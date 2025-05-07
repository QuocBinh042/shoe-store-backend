package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import lombok.Data;

@Data
public class OverviewCartItemResponse {
    private CartItemDTO cartItemDTO;
    private ProductDetailDTO productDetailDTO;
    private ProductDTO productDTO;
    private PromotionResponse promotion;
    private double discountPrice;
}
