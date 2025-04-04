package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.PromotionDTO;
import com.shoestore.Server.dto.response.ApiStatusResponse;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.PromotionResponse;
import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.enums.PromotionStatus;
import com.shoestore.Server.enums.PromotionType;
import com.shoestore.Server.service.ProductService;
import com.shoestore.Server.service.PromotionService;
import com.shoestore.Server.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<PromotionResponse>> getPromotionById(@PathVariable int id) {
        PromotionResponse response = promotionService.getPromotionById(id);
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), ApiStatusResponse.SUCCESS.getMessage(), null, response));
    }

    @GetMapping
    public ResponseEntity<RestResponse<PaginationResponse<PromotionResponse>>> getAllPromotions(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int size) {
        PaginationResponse<PromotionResponse> response = promotionService.getAllPromotions(page, size);
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), ApiStatusResponse.SUCCESS.getMessage(), null, response));
    }

    @PostMapping
    public ResponseEntity<RestResponse<PromotionResponse>> createPromotion(@Valid @RequestBody PromotionDTO promotionDTO) {
        PromotionResponse response = promotionService.createPromotion(promotionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RestResponse<>(ApiStatusResponse.CREATED.getCode(), ApiStatusResponse.CREATED.getMessage(), null, response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<PromotionResponse>> updatePromotion(@PathVariable int id, @Valid @RequestBody PromotionDTO promotionDTO) {
        PromotionResponse response = promotionService.updatePromotion(id, promotionDTO);
        return ResponseEntity.status(ApiStatusResponse.SUCCESS.getCode())
                .body(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), "Update promotion successfully", null, response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deletePromotion(@PathVariable int id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), "Promotion deleted", null, null));
    }

    @GetMapping("/by-product-id/{id}")
    public ResponseEntity<PromotionResponse> getPromotionByProduct(@PathVariable int id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(promotionService.getPromotionByProductID(product.getProductID()));
    }

    @GetMapping("/search")
    public ResponseEntity<RestResponse<PaginationResponse<PromotionResponse>>> searchPromotions(
            @RequestParam(required = false) PromotionStatus status,
            @RequestParam(required = false) PromotionType type,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize
    ) {
        PaginationResponse<PromotionResponse> response = promotionService.searchPromotions(status, type, name, startDate, endDate, page, pageSize);
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), ApiStatusResponse.SUCCESS.getMessage(), null, response));
    }
    @GetMapping("/count/upcoming")
    public ResponseEntity<RestResponse<Long>> countUpcomingPromotions() {
        long count = promotionService.countUpcomingPromotions();
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), "Count of upcoming promotions", null, count));
    }

    @GetMapping("/count/active")
    public ResponseEntity<RestResponse<Long>> countActivePromotions() {
        long count = promotionService.countActivePromotions();
        return ResponseEntity.ok(new RestResponse<>(ApiStatusResponse.SUCCESS.getCode(), "Count of active promotions", null, count));
    }
}
