package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.PromotionDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.PromotionResponse;
import com.shoestore.Server.entities.Category;
import com.shoestore.Server.entities.Promotion;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.enums.PromotionStatus;
import com.shoestore.Server.enums.PromotionType;
import com.shoestore.Server.exception.NotFoundException;
import com.shoestore.Server.mapper.PromotionMapper;
import com.shoestore.Server.repositories.CategoryRepository;
import com.shoestore.Server.repositories.PromotionRepository;
import com.shoestore.Server.repositories.ProductRepository;
import com.shoestore.Server.service.PaginationService;
import com.shoestore.Server.service.PromotionService;
import com.shoestore.Server.specifications.PromotionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final ProductRepository productRepository;
    private final PaginationService paginationService;
    private final CategoryRepository categoryRepository;

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
    public PromotionResponse getPromotionByProductID(int id) {
        log.info("Fetching promotion for Product ID: {}", id);
        Optional<Promotion> promotionOpt = promotionRepository.findPromotionByProductId(id);
        if (promotionOpt.isPresent()) {
            log.info("Promotion found for Product ID: {}", id);
            return promotionMapper.toResponse(promotionOpt.get());
        } else {
            log.warn("No promotion found for Product ID: {}", id);
            return null;
        }
    }

    @Override
    public PromotionResponse getPromotionById(int id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + id));
        return promotionMapper.toResponse(promotion);
    }

    @Override
    public PaginationResponse<PromotionResponse> getAllPromotions(int page, int size) {
        Pageable pageable = paginationService.createPageable(page, size);
        Page<Promotion> promotionPage = promotionRepository.findAll(pageable);
        return paginationService.paginate(promotionPage.map(promotionMapper::toResponse));
    }

    @Override
    @Transactional
    public PromotionResponse createPromotion(PromotionDTO promotionDTO) {
        log.info("Creating promotion: {}", promotionDTO.getName());

        Promotion promotion = promotionMapper.toEntity(promotionDTO);

        if (promotionDTO.getCategoryIDs() != null) {
            List<Category> categories = promotionDTO.getCategoryIDs().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Category not found with ID: " + id)))
                    .toList();
            promotion.setCategories(categories);
        } else {
            promotion.setCategories(new ArrayList<>());
        }

        if (promotionDTO.getApplicableProductIDs() != null) {
            List<Product> products = promotionDTO.getApplicableProductIDs().stream()
                    .map(id -> productRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id)))
                    .toList();
            promotion.setApplicableProducts(products);
        } else {
            promotion.setApplicableProducts(new ArrayList<>());
        }

        Promotion savedPromotion = promotionRepository.save(promotion);
        log.info("Promotion created successfully with ID: {}", savedPromotion.getPromotionID());

        return promotionMapper.toResponse(savedPromotion);
    }

    @Override
    @Transactional
    public PromotionResponse updatePromotion(int id, PromotionDTO promotionDTO) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promotion not found with ID: " + id));

        promotionMapper.updateEntityFromDto(promotionDTO, promotion);

        if (promotionDTO.getCategoryIDs() != null) {
            List<Category> categories = promotionDTO.getCategoryIDs().stream()
                    .map(cid -> categoryRepository.findById(cid)
                            .orElseThrow(() -> new NotFoundException("Category not found: " + cid)))
                    .collect(Collectors.toList());
            promotion.setCategories(categories);
        }

        if (promotionDTO.getApplicableProductIDs() != null) {
            List<Product> products = promotionDTO.getApplicableProductIDs().stream()
                    .map(pid -> productRepository.findById(pid)
                            .orElseThrow(() -> new NotFoundException("Product not found: " + pid)))
                    .collect(Collectors.toList());
            promotion.setApplicableProducts(products);
        }

        return promotionMapper.toResponse(promotionRepository.save(promotion));
    }


    @Override
    @Transactional
    public void deletePromotion(int id) {
        promotionRepository.deleteById(id);
    }

    @Override
    public PaginationResponse<PromotionResponse> searchPromotions(
            PromotionStatus status,
            PromotionType type,
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int pageSize
    ) {
        Specification<Promotion> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and(PromotionSpecification.hasStatus(status));
        }

        if (type != null) {
            spec = spec.and(PromotionSpecification.hasType(type));
        }

        if (name != null && !name.trim().isEmpty()) {
            spec = spec.and(PromotionSpecification.hasName(name));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(PromotionSpecification.inDateRange(startDate, endDate));
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Promotion> pagedPromotions = promotionRepository.findAll(spec, pageable);

        List<PromotionResponse> promotionResponses = pagedPromotions.getContent().stream()
                .map(promotionMapper::toResponse)
                .collect(Collectors.toList());

        return new PaginationResponse<>(
                promotionResponses,
                pagedPromotions.getTotalElements(),
                pagedPromotions.getTotalPages(),
                pagedPromotions.getNumber() + 1,
                pagedPromotions.getSize()
        );
    }

    @Override
    public long countUpcomingPromotions() {
        return promotionRepository.countByStatus(PromotionStatus.UPCOMING);
    }

    @Override
    public long countActivePromotions() {
        return promotionRepository.countByStatus(PromotionStatus.ACTIVE);
    }
}
