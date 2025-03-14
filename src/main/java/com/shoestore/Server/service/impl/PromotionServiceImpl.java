package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.PromotionDTO;
import com.shoestore.Server.entities.Promotion;
import com.shoestore.Server.mapper.PromotionMapper;
import com.shoestore.Server.repositories.PromotionRepository;
import com.shoestore.Server.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionServiceImpl(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Override
    public double getDiscountedPrice(ProductDTO product) {
        log.info("Calculating discounted price for Product ID: {}", product.getProductID());

        Optional<Promotion> optionalPromotion = promotionRepository.findPromotionByProductId(product.getProductID());

        if (optionalPromotion.isEmpty()) {
            log.info("No active promotion found for Product ID: {}", product.getProductID());
            return product.getPrice();
        }

        Promotion promotion = optionalPromotion.get();
        if (promotion.getEndDate().isBefore(LocalDateTime.now())) {
            log.info("Promotion for Product ID: {} has expired.", product.getProductID());
            return product.getPrice();
        }

        double discountPrice = product.getPrice();
        double discountValue = promotion.getDiscountValue().doubleValue();
        System.out.println("Discount Value: " + discountValue);

        discountPrice -= discountPrice * (discountValue / 100.0);
        log.info("Applied percentage discount: {}%, New price: {}", discountValue, discountPrice);

        double finalPrice = Math.max(discountPrice, 0.0);
        log.info("Final discounted price for Product ID {}: {}", product.getProductID(), finalPrice);
        return finalPrice;
    }

    @Override
    public PromotionDTO getPromotionByProductID(int id) {
        log.info("Fetching promotion for Product ID: {}", id);

        return promotionRepository.findPromotionByProductId(id)
                .map(promotion -> {
                    log.info("Promotion found for Product ID: {}", id);
                    return promotionMapper.toDto(promotion);
                })
                .orElseGet(() -> {
                    log.warn("No promotion found for Product ID: {}", id);
                    return null;
                });
    }

    @Override
    public PromotionDTO getPromotionById(int id) {
        log.info("Fetching promotion by Promotion ID: {}", id);

        return promotionRepository.findById(id)
                .map(promotion -> {
                    log.info("Found promotion with ID: {}", id);
                    return promotionMapper.toDto(promotion);
                })
                .orElseGet(() -> {
                    log.warn("No promotion found with ID: {}", id);
                    return null;
                });
    }
}
