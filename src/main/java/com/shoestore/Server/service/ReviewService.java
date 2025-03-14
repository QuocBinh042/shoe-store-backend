package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO getReview(int id);
    List<ReviewDTO> getReviewByProductId(int productId);
    ReviewDTO addReview(ReviewDTO reviewDTO);
}
