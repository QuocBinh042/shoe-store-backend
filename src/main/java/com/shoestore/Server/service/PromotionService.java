package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.PromotionDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.PromotionResponse;
import com.shoestore.Server.enums.PromotionStatus;
import com.shoestore.Server.enums.PromotionType;

import java.time.LocalDateTime;
import java.util.List;

public interface PromotionService {
    double getDiscountedPrice(int productID);

    PromotionResponse getPromotionByProductID(int id);

    PromotionResponse getPromotionById(int id);

    PaginationResponse<PromotionResponse> getAllPromotions(int page, int size);

    PromotionResponse createPromotion(PromotionDTO promotionDTO);

    PromotionResponse updatePromotion(int id, PromotionDTO promotionDTO);

    void deletePromotion(int id);

    PaginationResponse<PromotionResponse> searchPromotions(
            PromotionStatus status,
            PromotionType type,
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int pageSize
    );

    long countUpcomingPromotions();

    long countActivePromotions();

    List<PromotionResponse> getAppliedPromotionsForProduct(int productId);
}
