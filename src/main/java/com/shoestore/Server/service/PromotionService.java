package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.PromotionDTO;
import com.shoestore.Server.dto.response.ProductSearchResponse;

public interface PromotionService {
    double getDiscountedPrice(int productID);
    PromotionDTO getPromotionByProductID(int id);
    PromotionDTO getPromotionById(int id);
}
