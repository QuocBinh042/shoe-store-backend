package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailDTO {
    private int productDetailID;

    @NotBlank(message = "Color cannot be blank")
    private String color;

    @NotBlank(message = "Size cannot be blank")
    private String size;

    @NotNull(message = "Stock quantity cannot be null")
    @Min(value = 0, message = "Stock quantity cannot be less than 0")
    private Integer stockQuantity;
}
