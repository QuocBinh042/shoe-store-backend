package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.ReviewDTO;
import com.shoestore.Server.dto.response.ReviewResponse;
import com.shoestore.Server.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/by-product-id/{id}")
    public ResponseEntity<List<ReviewResponse>> getReviewByProduct(@PathVariable int id) {
        return ResponseEntity.ok(reviewService.getReviewByProductId(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ReviewDTO> addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.addReview(reviewDTO));
    }
    @GetMapping("/by-order-detail/{id}")
    public ResponseEntity<ReviewDTO> getReviewByOrderDetail(@PathVariable int id) {
        return ResponseEntity.ok(reviewService.getReviewByOrderDetail(id));
    }
}
