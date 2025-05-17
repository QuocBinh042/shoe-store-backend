package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.entities.Promotion;
import com.shoestore.Server.enums.PromotionType;
import lombok.Data;

import java.util.List;
@Data
public class SearchProductResponse {
    private int productID;
    private String productName;
    private String description;
    private double price;
    private String status;
    private List<ProductDetailsResponse> productDetails;
    private double discountPrice;
    private double rating;
    private PromotionResponse promotion;
    private String image;
}
