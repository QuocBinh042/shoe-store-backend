package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentDTO {
    private int paymentID;

    @NotNull(message = "Đơn hàng không được để trống")
    private OrderDTO order;

    @NotNull(message = "Ngày thanh toán không được để trống")
    private LocalDate paymentDate;

    @NotNull(message = "Trạng thái thanh toán không được để trống")
    private String status;
}
