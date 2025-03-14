package com.shoestore.Server.dto.request;

import lombok.Data;

@Data
public class ProductDetailDTO {
    private int productDetailID;
    private String color;
    private String size;
    private int stockQuantity;

}

