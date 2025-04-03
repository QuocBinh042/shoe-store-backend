package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.OrderDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class PaymentResponse {
    private int paymentID;

    @NotNull(message = "Payment date cannot be null")
    private LocalDate paymentDate;

    @NotNull(message = "Payment status cannot be null")
    private String status;
}
