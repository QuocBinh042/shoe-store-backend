package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.VoucherDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderResponse  {
    private int orderID;
    private LocalDate orderDate;

    private String status;

    private double total;

    private double feeShip;

    private String code;

    private String shippingAddress;

    private String typePayment;

    private double discount;

    private VoucherDTO voucher;

    private UserResponse user;
}
