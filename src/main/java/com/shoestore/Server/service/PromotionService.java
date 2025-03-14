package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.PromotionDTO;

public interface PromotionService {
    double getDiscountedPrice(ProductDTO product);
    PromotionDTO getPromotionByProductID(int id);
    PromotionDTO getPromotionById(int id);
}
