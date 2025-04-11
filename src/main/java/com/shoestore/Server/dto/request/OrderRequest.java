package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderRequest {
    private int orderID;

    @PastOrPresent(message = "Order date cannot be in the future")
    private LocalDate orderDate;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotNull(message = "Total amount cannot be null")
    @PositiveOrZero(message = "Total amount cannot be negative")
    private double total;

    @NotNull(message = "Shipping fee cannot be null")
    @PositiveOrZero(message = "Shipping fee cannot be negative")
    private double feeShip;

    @NotBlank(message = "Order code cannot be blank")
    private String code;

    @NotBlank(message = "Shipping address cannot be blank")
    private String shippingAddress;

    @NotBlank(message = "Payment method cannot be blank")
    private String typePayment;

    @NotNull(message = "Discount cannot be null")
    @PositiveOrZero(message = "Discount cannot be negative")
    private double discount;

    private VoucherDTO voucher;
    private UserDTO user;
}
