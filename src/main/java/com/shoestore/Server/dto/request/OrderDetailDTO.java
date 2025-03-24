package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDetailDTO {
    private int orderDetailID;

    @NotNull(message = "Đơn hàng không được để trống")
    private OrderDTO order;

    @NotNull(message = "Chi tiết sản phẩm không được để trống")
    private ProductDetailDTO productDetail;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá không được âm")
    private Double price;
}
