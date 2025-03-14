package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import lombok.Data;

@Data
public class CartItemResponse {
    private CartItemDTO cartItemDTO;
    private String productName;
    private ProductDetailDTO productDetailDTO;
    private double productPrice;
}
