package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentDTO {
    private int paymentID;

    @NotNull(message = "Order cannot be null")
    private OrderDTO order;

    @NotNull(message = "Payment date cannot be null")
    private LocalDate paymentDate;

    @NotNull(message = "Payment status cannot be null")
    private String status;
}
