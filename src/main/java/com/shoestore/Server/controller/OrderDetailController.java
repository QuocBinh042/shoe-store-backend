package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.response.OrderDetailResponse;
import com.shoestore.Server.dto.response.OrderResponse;
import com.shoestore.Server.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping("/by-order-id/{order-id}")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetailByOrder(@PathVariable("order-id") int id) {
        return ResponseEntity.ok(orderDetailService.getProductDetailByOrderID(id));
    }

    @GetMapping("/order/{orderId}/details")
    public ResponseEntity<List<OrderDetailResponse>> getOrderDetailByOrderID(@PathVariable("orderId") int orderId) {
        return ResponseEntity.ok(orderDetailService.getOrderDetailByOrderID(orderId));
    }


    @PostMapping("/add")
    public ResponseEntity<OrderDetailDTO> addOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok(orderDetailService.save(orderDetailDTO));
    }
}