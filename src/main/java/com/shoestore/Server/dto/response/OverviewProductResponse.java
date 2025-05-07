package com.shoestore.Server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class OverviewProductResponse {
    private List<ProductDetailsResponse> productDetails;
    private String productName;
    private String categoryName;
    private String brandName;
    private String description;
    private double price;
    private List<String> imageURL;
    private double discountPrice;
    private PromotionResponse promotion;
}
