package com.shoestore.Server.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class FeaturedProductResponse {
    private int productID;
    private String productName;
    private String description;
    private double price;
    private long totalQuantity;
    private long viewCount;
    private LocalDateTime createdAt;
    private String imageURLs;

    public FeaturedProductResponse(int productID, String productName, String description, double price, long totalQuantity, long viewCount, LocalDateTime createdAt, String imageURLs) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.imageURLs = imageURLs;
    }
}
