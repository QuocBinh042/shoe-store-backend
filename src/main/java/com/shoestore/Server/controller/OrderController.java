package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.response.OrderResponse;
import com.shoestore.Server.service.EmailService;
import com.shoestore.Server.service.MailService;
import com.shoestore.Server.service.OrderService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final EmailService emailService;
    @PreAuthorize("hasAnyAuthority('CREATE_ORDER')")
    @PostMapping("/add")
    public ResponseEntity<OrderDTO> addOrder(@Valid @RequestBody OrderDTO orderDTO){
        System.out.println(orderDTO);
        OrderDTO saveOrder=orderService.addOrder(orderDTO);
//        emailService.sendOrderSuccessEmail(saveOrder.getUser().getEmail(), saveOrder.getUser().getName(), saveOrder.getCode());
        return ResponseEntity.ok(saveOrder);
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
    @PreAuthorize("hasAnyAuthority('VIEW_ORDER')")
    @GetMapping("/get-all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable int userId) {
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
