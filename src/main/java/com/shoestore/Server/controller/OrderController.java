package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.response.*;
import com.shoestore.Server.service.EmailService;
import com.shoestore.Server.service.OrderService;
import com.shoestore.Server.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @PostMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable int id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<PlacedOrderResponse>> getOrdersByUserId(@PathVariable int userId) {
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

    @GetMapping("/month")
    public ResponseEntity<PaginationResponse<OrderDTO>> getOrdersByMonth(
            @RequestParam(value = "page", defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        PaginationResponse<OrderDTO> response = orderService.getOrdersByMonth(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/year")
    public ResponseEntity<PaginationResponse<OrderDTO>> getOrdersByYear(
            @RequestParam(value = "page", defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        PaginationResponse<OrderDTO> response = orderService.getOrdersByYear(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/revenue/with-promotions")
    public ResponseEntity<RestResponse<BigDecimal>> getRevenueFromPromotions() {
        BigDecimal revenue = orderService.getRevenueFromPromotions();
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), "Revenue from promotions", null, revenue));
    }

    @GetMapping("/count/with-promotions")
    public ResponseEntity<RestResponse<Long>> countOrdersWithPromotions() {
        long count = orderService.countOrdersWithPromotions();
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), "Count of orders with promotions", null, count));
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (page != null && pageSize != null) {
            PaginationResponse<OrderDTO> response = orderService.getAllOrdersPaged(page, pageSize);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(orderService.getAllOrders());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<PaginationResponse<OrderResponse>> filterOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String mode
    ) {
        PaginationResponse<OrderResponse> result = orderService.filterOrders(
                status, q, from, to, page, pageSize, sort, mode
        );
        return ResponseEntity.ok(result);
    }
}
