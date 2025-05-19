package com.shoestore.Server.dto.request;

import com.shoestore.Server.enums.Color;
import com.shoestore.Server.enums.ProductStatus;
import com.shoestore.Server.enums.Size;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class ProductDetailRequest {
    private int productDetailID;

    private Color color;

    private Size size;

    @DecimalMin(value = "0", inclusive = false, message = "Stock quantity must be greater than 0")
    private int stockQuantity;

    private ProductStatus status;

    private String image;

}
