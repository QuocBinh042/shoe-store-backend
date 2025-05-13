package com.shoestore.Server.service.impl;

import com.shoestore.Server.entities.Order;
import com.shoestore.Server.enums.OrderStatus;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.service.EmailService;
import com.shoestore.Server.service.ProductService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    @Value("${app.mail.from.address}")
    private String fromAddress;
    @Value("${app.mail.from.name}")
    private String fromName;
    @Async
    @Override
    public void sendVerificationCodeEmail(String to, String customerName) {
        try {
            String verificationCode = generateVerificationCode();
            redisTemplate.opsForValue().set("OTP:" + to, verificationCode, 5, TimeUnit.MINUTES);
            logger.info("Stored OTP: {} for email: {} in Redis", verificationCode, to);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Xác thực tài khoản của bạn");

            Context context = new Context();
            context.setVariable("customerName", customerName);
            context.setVariable("verificationCode", verificationCode);
            String htmlContent = templateEngine.process("verify-email-template", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            logger.info("Verification email sent to {}", to);
        } catch (Exception e) {
            logger.error("Error sending email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    @Async
    @Override
    public void sendOrderSuccessEmail(String to, String customerName, String orderCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Đặt hàng thành công - Mã đơn hàng: " + orderCode);
            Context context = new Context();
            context.setVariable("customerName", customerName);
            context.setVariable("code", orderCode);
            String htmlContent = templateEngine.process("email-thymeleaf", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Override
    public void sendOrderStatusEmail(int orderId, OrderStatus status) {
        try {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

            // Set From/Reply-To
            helper.setFrom(new InternetAddress(fromAddress, fromName));
            helper.setReplyTo(fromAddress);

            // Set To & Subject
            helper.setTo(order.getUser().getEmail());
            System.out.println(status);
            String subject = switch (status) {
                case CONFIRMED -> "Order Confirmed – Code: " + order.getCode();
                case CANCELED -> "Order Canceled  – Code: " + order.getCode();
                default -> "Order Update    – Code: " + order.getCode();
            };
            helper.setSubject(subject);

            // Build context
            Context ctx = new Context();
            ctx.setVariable("customerName", order.getUser().getName());
            ctx.setVariable("orderCode", order.getCode());
            ctx.setVariable("status", status.name());
            ctx.setVariable("orderDate", order.getOrderDate().toString());
            ctx.setVariable("shippingFee", order.getFeeShip());
            ctx.setVariable("totalPrice", order.getTotal());

            // Map items with product name lookup
            var emailItems = order.getOrderDetails().stream().map(detail -> {
                var prodDto = productService.getProductByProductDetailsId(detail.getProductDetail().getProductDetailID());
                Map<String, Object> m = new HashMap<>();
                m.put("productName", prodDto.getProductName());
                m.put("color", detail.getProductDetail().getColor());
                m.put("size", detail.getProductDetail().getSize());
                m.put("quantity", detail.getQuantity());
                m.put("price", detail.getPrice());
                return m;
            }).collect(Collectors.toList());
            ctx.setVariable("items", emailItems);

            // Render HTML & text
            String html = templateEngine.process("email/order-status", ctx);
            String text = new StringBuilder().append("Hello ").append(order.getUser().getName()).append("\n").append("Order Code: ").append(order.getCode()).append("\n").append("Order Date: ").append(order.getOrderDate()).append("\n\n").append("Thank you for shopping with us!").toString();
            helper.setText(text, html);

            mailSender.send(msg);
            logger.info("Sent order status email to {}", order.getUser().getEmail());

        } catch (Exception ex) {
            logger.error("Failed to send order status email for order {}", orderId, ex);
        }
    }

}
