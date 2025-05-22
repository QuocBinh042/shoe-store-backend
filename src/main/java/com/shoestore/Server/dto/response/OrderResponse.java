package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.VoucherDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse  {
    private int orderID;
    private LocalDate orderDate;

    private String status;

    private double total;

    private double feeShip;

    private String code;

    private String shippingAddress;

    private String paymentMethod;

    private double voucherDiscount;

    private VoucherDTO voucher;

    private UserResponse user;
    private LocalDateTime createdAt;
    private List<OrderStatusHistoryResponse> statusHistory;
}
