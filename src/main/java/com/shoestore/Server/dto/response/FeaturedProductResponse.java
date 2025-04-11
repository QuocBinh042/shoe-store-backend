package com.shoestore.Server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class FeaturedProductResponse {
    private int productID;
    private String productName;
    private String description;
    private double price;
    private long totalQuantity;
    private long viewCount;
    private LocalDateTime createdAt;
    private String imageURLs;
}
