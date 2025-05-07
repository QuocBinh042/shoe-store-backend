package com.shoestore.Server.service.impl;

import com.shoestore.Server.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, StringRedisTemplate redisTemplate) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.redisTemplate = redisTemplate;
    }

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
            logger.info("Order success email sent to {}", to);
        } catch (Exception e) {
            logger.error("Error sending order email to {}: {}", to, e.getMessage());
            e.printStackTrace();
        }
    }
}