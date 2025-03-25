package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.PromotionDTO;
import com.shoestore.Server.dto.response.ProductSearchResponse;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.entities.Promotion;
import com.shoestore.Server.mapper.PromotionMapper;
import com.shoestore.Server.repositories.ProductRepository;
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
    private final ProductRepository productRepository;
    public PromotionServiceImpl(PromotionRepository promotionRepository, PromotionMapper promotionMapper, ProductRepository productRepository) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
        this.productRepository = productRepository;
    }

    @Override
    public double getDiscountedPrice(int productID) {
        log.info("Calculating discounted price for Product ID: {}", productID);

        Optional<Product> optionalProduct = productRepository.findById(productID);
        if (optionalProduct.isEmpty()) {
            log.warn("Product with ID {} not found!", productID);
            return 0.0;
        }
        Product product = optionalProduct.get();
        Optional<Promotion> optionalPromotion = promotionRepository.findPromotionByProductId(productID);

        if (optionalPromotion.isEmpty()) {
            log.info("No active promotion found for Product ID: {}", productID);
            return product.getPrice();
        }

        Promotion promotion = optionalPromotion.get();
        if (promotion.getEndDate().isBefore(LocalDateTime.now())) {
            log.info("Promotion for Product ID: {} has expired.", productID);
            return product.getPrice();
        }

        double discountPrice = product.getPrice();
        double discountValue = promotion.getDiscountValue().doubleValue();

        discountPrice -= discountPrice * (discountValue / 100.0);
        log.info("Applied percentage discount: {}%, New price: {}", discountValue, discountPrice);

        double finalPrice = Math.max(discountPrice, 0.0);
        log.info("Final discounted price for Product ID {}: {}", productID, finalPrice);
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
