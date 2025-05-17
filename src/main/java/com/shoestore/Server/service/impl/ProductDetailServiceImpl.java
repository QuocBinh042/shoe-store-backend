package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.request.ProductDetailRequest;
import com.shoestore.Server.dto.response.OverviewProductResponse;
import com.shoestore.Server.dto.response.ProductDetailsResponse;
import com.shoestore.Server.dto.response.PromotionResponse;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.entities.ProductDetail;

import com.shoestore.Server.enums.Color;
import com.shoestore.Server.enums.Size;
import com.shoestore.Server.mapper.ProductDetailMapper;
import com.shoestore.Server.mapper.ProductMapper;
import com.shoestore.Server.repositories.ProductDetailRepository;
import com.shoestore.Server.repositories.ProductRepository;
import com.shoestore.Server.service.ProductDetailService;
import com.shoestore.Server.service.ProductService;
import com.shoestore.Server.service.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailMapper productDetailMapper;
    private final ProductRepository productRepository;
    private final PromotionService promotionService;

    @Override
    public List<ProductDetailsResponse> getByProductId(int productID) {
        log.info("Fetching product details for Product ID: {}", productID);

        List<ProductDetail> productDetails = productDetailRepository.findByProduct_ProductID(productID);
        log.info("Found {} product details for Product ID: {}", productDetails.size(), productID);

        return productDetails.stream()
                .map(productDetailMapper::toResponse)
                .toList();
    }

    @Override
    public ProductDetailsResponse getProductDetailById(int id) {
        log.info("Fetching product detail with ID: {}", id);

        return productDetailRepository.findById(id)
                .map(productDetail -> {
                    log.info("Found product detail with ID: {}", id);
                    return productDetailMapper.toResponse(productDetail);
                })
                .orElseGet(() -> {
                    log.warn("Product detail with ID {} not found", id);
                    return null;
                });
    }

    @Override
    public OverviewProductResponse getProductOverviewById(int productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return null;

        List<ProductDetailsResponse> productDetails = getByProductId(productId);
        String categoryName = product.getCategory().getName();
        String brandName = product.getBrand().getName();
        PromotionResponse promotion = promotionService.getPromotionByProductID(productId);

        return new OverviewProductResponse(
                productDetails,
                product.getProductName(),
                categoryName,
                brandName,
                product.getDescription(),
                product.getPrice(),
                promotionService.getDiscountedPrice(productId),
                promotion
        );
    }



    @Override
    @Transactional
    public ProductDetailsResponse createProductDetail(int productId, ProductDetailRequest productDetailRequest) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            ProductDetail productDetail = productDetailMapper.toEntity(productDetailRequest);
            productDetail.setProduct(productOpt.get());

            ProductDetail savedDetail = productDetailRepository.save(productDetail);
            return productDetailMapper.toResponse(savedDetail);
        }

        return null;
    }

    @Override
    @Transactional
    public ProductDetailsResponse updateProductDetail(int productDetailId, ProductDetailRequest productDetailRequest) {
        Optional<ProductDetail> existingDetail = productDetailRepository.findById(productDetailId);

        if (existingDetail.isPresent()) {
            ProductDetail detail = existingDetail.get();

            detail.setColor(productDetailRequest.getColor());
            detail.setSize(productDetailRequest.getSize());
            detail.setStockQuantity(productDetailRequest.getStockQuantity());

            ProductDetail updatedDetail = productDetailRepository.save(detail);
            return productDetailMapper.toResponse(updatedDetail);
        }

        return null;
    }
}
