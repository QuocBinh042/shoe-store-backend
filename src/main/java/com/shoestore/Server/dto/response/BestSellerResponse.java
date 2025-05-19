package com.shoestore.Server.dto.response;

public record BestSellerResponse(
        int productId,
        String name,
        String imageUrl,
        long sold,
        double price,
        double revenue,
        double revenuePercent,   // % đóng góp so với tổng revenue
        double growthPercent,    // % tăng/giảm so với kỳ trước
        int stock,
        double avgRating
) {}