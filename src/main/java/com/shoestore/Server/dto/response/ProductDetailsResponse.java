package com.shoestore.Server.dto.response;

import com.shoestore.Server.enums.Color;
import com.shoestore.Server.enums.ProductStatus;
import com.shoestore.Server.enums.Size;
import lombok.Data;

@Data
public class ProductDetailsResponse {
    private int productDetailID;

    private Color color;

    private Size size;

    private int stockQuantity;

    private ProductStatus status;

    private String image;
}
