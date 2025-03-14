package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.service.OrderDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/by-order-id/{order-id}")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetailByOrder(@PathVariable("order-id") int id) {
        return ResponseEntity.ok(orderDetailService.getProductDetailByOrderID(id));
    }

    @PostMapping("/add")
    public ResponseEntity<OrderDetailDTO> addOrderDetail(@RequestBody OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok(orderDetailService.save(orderDetailDTO));
    }
}