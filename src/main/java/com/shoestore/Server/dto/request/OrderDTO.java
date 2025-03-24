package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderDTO {
    private int orderID;

    @PastOrPresent(message = "Ngày đặt hàng không được ở tương lai")
    private LocalDate orderDate;

    @NotBlank(message = "Trạng thái không được để trống")
    private String status;
    @NotNull(message = "Tổng tiền không được để trống")
    @PositiveOrZero(message = "Tổng tiền không được âm")
    private double total;
    @NotNull(message = "Phi ship không được để trống")
    @PositiveOrZero(message = "Phí ship không được âm")
    private double feeShip;

    @NotBlank(message = "Mã đơn hàng không được để trống")
    private String code;

    private VoucherDTO voucher;

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    private String shippingAddress;

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    private String typePayment;
    @NotNull(message = "Giam gia không được để trống")
    @PositiveOrZero(message = "Giảm giá không được âm")
    private double discount;

    @NotNull(message = "Người đặt hàng không được để trống")
    private UserDTO user;
}
