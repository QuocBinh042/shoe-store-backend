package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.PromotionDTO;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.service.ProductService;
import com.shoestore.Server.service.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {
    private final PromotionService promotionService;
    private final ProductService productService;

    public PromotionController(PromotionService promotionService, ProductService productService) {
        this.promotionService = promotionService;
        this.productService = productService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getPromotionByID(@PathVariable int id) {
        PromotionDTO promotion = promotionService.getPromotionById(id);
        return promotion != null ? ResponseEntity.ok(promotion) : ResponseEntity.notFound().build();
    }
    @GetMapping("/discount-price/by-product-id/{id}")
    public ResponseEntity<Double> getDiscountPriceByProduct(@PathVariable int id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        double discountedPrice = promotionService.getDiscountedPrice(product);
        return ResponseEntity.ok(discountedPrice);
    }

    @GetMapping("/by-product-id/{id}")
    public ResponseEntity<PromotionDTO> getPromotionByProduct(@PathVariable int id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(promotionService.getPromotionByProductID(product.getProductID()));
    }


}
