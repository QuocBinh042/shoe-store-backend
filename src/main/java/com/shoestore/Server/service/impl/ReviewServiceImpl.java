package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ReviewDTO;
import com.shoestore.Server.dto.response.ReviewResponse;
import com.shoestore.Server.entities.*;
import com.shoestore.Server.mapper.ReviewMapper;
import com.shoestore.Server.repositories.*;
import com.shoestore.Server.service.ProductDetailService;
import com.shoestore.Server.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper, OrderDetailRepository orderDetailRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    @Override
    public ReviewDTO getReview(int id) {
        log.info("Fetching review with ID: {}", id);
        return reviewRepository.findById(id)
                .map(reviewMapper::toDto)
                .orElseGet(() -> {
                    log.warn("Review not found with ID: {}", id);
                    return null;
                });
    }

    @Override
    public List<ReviewResponse> getReviewByProductId(int productId) {
        log.info("Fetching reviews for Product ID: {}", productId);
        List<Review> reviews = reviewRepository.findReviewsByProduct_ProductID(productId);
        log.info("Found {} reviews for Product ID: {}", reviews.size(), productId);

        return reviews.stream().map(review -> {
            ReviewResponse response = reviewMapper.toReviewResponse(review);
            if (review.getOrderDetail() != null && review.getOrderDetail().getProductDetail() != null) {
                ProductDetail productDetail = review.getOrderDetail().getProductDetail();
                response.setProductDetailsColor(productDetail.getColor().getColorName());
                response.setProductDetailsSize(productDetail.getSize().name());
            }
            return response;
        }).collect(Collectors.toList());
    }


    @Override
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        log.info("Adding a new review for Order details ID: {}, Product ID: {}",
                reviewDTO.getOrderDetail().getOrderDetailID(), reviewDTO.getProduct().getProductID());
        Review review = reviewMapper.toEntity(reviewDTO);
        OrderDetail orderDetail = orderDetailRepository.findById(reviewDTO.getOrderDetail().getOrderDetailID())
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", reviewDTO.getOrderDetail().getOrderDetailID());
                    return new IllegalArgumentException("Order not found");
                });

        Product product = productRepository.findProductByOrderDetailId(orderDetail.getOrderDetailID());
        User user = userRepository.findById(reviewDTO.getUser().getUserID())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", reviewDTO.getUser().getUserID());
                    return new IllegalArgumentException("User not found");
                });
        review.setOrderDetail(orderDetail);
        review.setProduct(product);
        review.setUser(user);
        Review savedReview = reviewRepository.save(review);
        log.info("Successfully added review with ID: {}", savedReview.getReviewID());
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDTO getReviewByOrderDetail(int orderDetailId) {
        return reviewRepository.findByOrderDetail_OrderDetailID(orderDetailId)
                .map(reviewMapper::toDto)
                .orElse(null);
    }


}
