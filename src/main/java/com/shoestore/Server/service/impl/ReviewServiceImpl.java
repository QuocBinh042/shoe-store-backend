package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ReviewDTO;
import com.shoestore.Server.entities.Order;
import com.shoestore.Server.entities.ProductDetail;
import com.shoestore.Server.entities.Review;
import com.shoestore.Server.mapper.ReviewMapper;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.ProductDetailRepository;
import com.shoestore.Server.repositories.ReviewRepository;
import com.shoestore.Server.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final OrderRepository orderRepository;
    private final ProductDetailRepository productDetailRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper,
                             OrderRepository orderRepository, ProductDetailRepository productDetailRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.orderRepository = orderRepository;
        this.productDetailRepository = productDetailRepository;
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
    public List<ReviewDTO> getReviewByProductId(int productId) {
        log.info("Fetching reviews for Product ID: {}", productId);
        List<Review> reviews = reviewRepository.findReviewsByProductID(productId);
        log.info("Found {} reviews for Product ID: {}", reviews.size(), productId);
        return reviewMapper.toDtoList(reviews);
    }

    @Override
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        log.info("Adding a new review for Order ID: {}, Product Detail ID: {}",
                reviewDTO.getOrder().getOrderID(), reviewDTO.getProductDetail().getProductDetailID());

        Review review = reviewMapper.toEntity(reviewDTO);

        Order order = orderRepository.findById(reviewDTO.getOrder().getOrderID())
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", reviewDTO.getOrder().getOrderID());
                    return new IllegalArgumentException("Order not found");
                });

        ProductDetail productDetail = productDetailRepository.findById(reviewDTO.getProductDetail().getProductDetailID())
                .orElseThrow(() -> {
                    log.error("Product detail not found with ID: {}", reviewDTO.getProductDetail().getProductDetailID());
                    return new IllegalArgumentException("Product detail not found");
                });

        review.setOrder(order);
        review.setProductDetail(productDetail);
        Review savedReview = reviewRepository.save(review);

        log.info("Successfully added review with ID: {}", savedReview.getReviewID());
        return reviewMapper.toDto(savedReview);
    }
}
