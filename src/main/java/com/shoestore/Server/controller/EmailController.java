package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.request.OrderDetailRequest;
import com.shoestore.Server.dto.request.OrderEmailRequest;
import com.shoestore.Server.dto.request.ProductDetailRequest;
import com.shoestore.Server.entities.Order;
import com.shoestore.Server.enums.OrderStatus;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.ProductDetailRepository;
import com.shoestore.Server.service.EmailService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final ProductDetailRepository productDetailRepository;

    @PostMapping("/order-status/{orderId}")
    public ResponseEntity<Void> sendOrderStatusEmail(
            @PathVariable int orderId,
            @RequestParam @NotBlank String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        emailService.sendOrderStatusEmail(orderId, orderStatus);
        return ResponseEntity.ok().build();
    }
}
