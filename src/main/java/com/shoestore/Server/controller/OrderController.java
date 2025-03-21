package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.service.MailService;
import com.shoestore.Server.service.OrderService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final MailService mailService;

    public OrderController(OrderService orderService, MailService mailService) {
        this.orderService = orderService;
        this.mailService = mailService;
    }

    @PostMapping("/add")
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.addOrder(orderDTO));
    }

    @PostMapping("/mail-confirm")
    public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        try {
            mailService.sendOrderConfirmation(to, subject, text);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return orderDTO != null ? ResponseEntity.ok(orderDTO) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/update-status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@RequestParam int orderId, @RequestParam String status) {

        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
    @PreAuthorize("hasAnyAuthority('MANAGE_ORDERS')")
    @GetMapping("/get-all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(orderService.getOrderByByUser(userId));
    }

    @GetMapping("/count-total-order/{user-id}")
    public ResponseEntity<Integer> countUserOrders(@PathVariable("user-id") int id) {
        return ResponseEntity.ok(orderService.getOrderQuantityByUserId(id));
    }

    @GetMapping("/total-cost-order/{user-id}")
    public ResponseEntity<Double> getTotalSpent(@PathVariable("user-id") int id) {
        return ResponseEntity.ok(orderService.getTotalAmountByUserId(id));
    }
}
