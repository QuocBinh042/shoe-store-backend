package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderDTO {
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

    private VoucherDTO voucher;

    @NotBlank(message = "Shipping address cannot be blank")
    private String shippingAddress;

    @NotBlank(message = "Payment method cannot be blank")
    private String typePayment;

    @NotNull(message = "Discount cannot be null")
    @PositiveOrZero(message = "Discount cannot be negative")
    private double discount;

    @NotNull(message = "User cannot be null")
    private UserDTO user;
}
