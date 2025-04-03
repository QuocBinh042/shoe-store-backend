package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.PromotionDTO;

public interface PromotionService {
    double getDiscountedPrice(int productID);
    PromotionDTO getPromotionByProductID(int id);
    PromotionDTO getPromotionById(int id);
}
