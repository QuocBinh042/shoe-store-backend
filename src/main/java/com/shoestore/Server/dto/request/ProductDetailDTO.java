package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailDTO {
    private int productDetailID;

    @NotBlank(message = "Màu sắc không được để trống")
    private String color;

    @NotBlank(message = "Kích thước không được để trống")
    private String size;

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho không thể nhỏ hơn 0")
    private Integer stockQuantity;
}
