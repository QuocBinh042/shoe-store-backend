package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDTO;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final EmailService emailService;

    @PreAuthorize("hasAnyAuthority('CREATE_ORDER')")
    public ResponseEntity<OrderDTO> addOrder(@Valid @RequestBody OrderDTO orderDTO){
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

    @GetMapping("/users/{userId}/count")
    public ResponseEntity<Integer> countUserOrders(@PathVariable int userId) {
        return ResponseEntity.ok(orderService.getOrderQuantityByUserId(userId));
    }

    @GetMapping("/users/{userId}/total-cost")
    public ResponseEntity<Double> getTotalSpent(@PathVariable int userId) {
        return ResponseEntity.ok(orderService.getTotalAmountByUserId(userId));
    }

    @GetMapping("/total-orders/day")
    public ResponseEntity<Long> getTotalOrdersByDay() {
        long totalOrders = orderService.getTotalOrdersByDay();
        return ResponseEntity.ok(totalOrders);
    }

    @GetMapping("/total-orders/month")
    public ResponseEntity<Long> getTotalOrdersByMonth() {
        long totalOrders = orderService.getTotalOrdersByMonth();
        return ResponseEntity.ok(totalOrders);
    }

    @GetMapping("/total-orders/year")
    public ResponseEntity<Long> getTotalOrdersByYear() {
        long totalOrders = orderService.getTotalOrdersByYear();
        return ResponseEntity.ok(totalOrders);
    }

    @GetMapping("/total-orders")
    public ResponseEntity<Long> getTotalOrders() {
        long totalOrders = orderService.getTotalOrders();
        return ResponseEntity.ok(totalOrders);
    }

    @GetMapping("/total-amount")
    public ResponseEntity<Double> getTotalOrderAmount() {
        Double totalAmount = orderService.getTotalOrderAmount();
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-amount/day")
    public ResponseEntity<Double> getTotalOrderAmountByDay() {
        Double totalAmount = orderService.getTotalOrderAmountByDay();
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-amount/month")
    public ResponseEntity<Double> getTotalOrderAmountByMonth() {
        Double totalAmount = orderService.getTotalOrderAmountByMonth();
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-amount/year")
    public ResponseEntity<Double> getTotalOrderAmountByYear() {
        Double totalAmount = orderService.getTotalOrderAmountByYear();
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/completed-orders")
    public ResponseEntity<Long> getCompletedOrders() {
        long count = orderService.getCompletedOrders();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/completed-orders/day")
    public ResponseEntity<Long> getCompletedOrdersByDay() {
        long count = orderService.getCompletedOrdersByDay();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/completed-orders/month")
    public ResponseEntity<Long> getCompletedOrdersByMonth() {
        long count = orderService.getCompletedOrdersByMonth();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/completed-orders/year")
    public ResponseEntity<Long> getCompletedOrdersByYear() {
        long count = orderService.getCompletedOrdersByYear();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/canceled-orders")
    public ResponseEntity<Long> getCanceledOrders() {
        long count = orderService.getCanceledOrders();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/canceled-orders/day")
    public ResponseEntity<Long> getCanceledOrdersByDay() {
        long count = orderService.getCanceledOrdersByDay();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/canceled-orders/month")
    public ResponseEntity<Long> getCanceledOrdersByMonth() {
        long count = orderService.getCanceledOrdersByMonth();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/canceled-orders/year")
    public ResponseEntity<Long> getCanceledOrdersByYear() {
        long count = orderService.getCanceledOrdersByYear();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/total-cost-order/{user-id}")
    public ResponseEntity<Double> getTotalSpent(@PathVariable("user-id") int id) {
        return ResponseEntity.ok(orderService.getTotalAmountByUserId(id));
    }
}
