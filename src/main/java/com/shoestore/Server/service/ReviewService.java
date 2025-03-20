package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.ReviewDTO;
import com.shoestore.Server.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewDTO getReview(int id);
    List<ReviewResponse> getReviewByProductId(int productId);
    ReviewDTO addReview(ReviewDTO reviewDTO);
    ReviewDTO getReviewByOrderDetail(int orderDetailId);
}
