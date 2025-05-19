package com.shoestore.Server.dto.request;

import com.shoestore.Server.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class OrderEmailRequest {
    @NotBlank
    private String to;

    @NotBlank
    private String customerName;

    @NotBlank
    private String orderCode;

    @NotBlank
    private OrderStatus status;

    private double totalPrice;
    private double shippingFee;
    private String orderDate;
    private List<OrderDetailRequest> items;
}
