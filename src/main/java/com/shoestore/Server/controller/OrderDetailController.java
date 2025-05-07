package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.response.PlacedOrderDetailsResponse;
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

//    @GetMapping("/by-order-id/{order-id}")
//    public ResponseEntity<List<OrderDetailsResponse>> getOrderDetailByOrder(@PathVariable("order-id") int id) {
//        return ResponseEntity.ok(orderDetailService.getProductDetailByOrderID(id));
//    }

    @GetMapping("/order/{orderId}/details")
    public ResponseEntity<List<PlacedOrderDetailsResponse>> getOrderDetailByOrderID(@PathVariable("orderId") int orderId) {
        return ResponseEntity.ok(orderDetailService.getOrderDetailByOrderID(orderId));
    }


    @PostMapping("/add")
    public ResponseEntity<OrderDetailDTO> addOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        System.out.println(orderDetailDTO);
        return ResponseEntity.ok(orderDetailService.save(orderDetailDTO));
    }
}