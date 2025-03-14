package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.entities.Color;
import com.shoestore.Server.entities.ProductDetail;
import com.shoestore.Server.entities.Size;
import com.shoestore.Server.mapper.ProductDetailMapper;
import com.shoestore.Server.repositories.ProductDetailRepository;
import com.shoestore.Server.service.ProductDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailMapper productDetailMapper;

    public ProductDetailServiceImpl(ProductDetailRepository productDetailRepository, ProductDetailMapper productDetailMapper) {
        this.productDetailRepository = productDetailRepository;
        this.productDetailMapper = productDetailMapper;
    }

    @Override
    public ProductDetailDTO addProductDetail(ProductDetailDTO productDetailDTO) {
        ProductDetail productDetail = productDetailMapper.toEntity(productDetailDTO);
        ProductDetail savedProductDetail = productDetailRepository.save(productDetail);

        log.info("Added product detail with ID: {}", savedProductDetail.getProductDetailID());
        return productDetailMapper.toDto(savedProductDetail);
    }

    @Override
    public List<ProductDetailDTO> getByProductId(int productID) {
        log.info("Fetching product details for Product ID: {}", productID);

        List<ProductDetail> productDetails = productDetailRepository.findByProduct_ProductID(productID);
        log.info("Found {} product details for Product ID: {}", productDetails.size(), productID);

        return productDetails.stream()
                .map(productDetailMapper::toDto)
                .toList();
    }

    @Override
    public ProductDetailDTO save(ProductDetailDTO productDetailDTO) {
        if (productDetailDTO == null) {
            log.error("Attempted to save a null ProductDetailDTO");
            throw new IllegalArgumentException("ProductDetail không được để trống.");
        }

        ProductDetail productDetail = productDetailMapper.toEntity(productDetailDTO);
        ProductDetail savedProductDetail = productDetailRepository.save(productDetail);

        log.info("Saved product detail with ID: {}", savedProductDetail.getProductDetailID());
        return productDetailMapper.toDto(savedProductDetail);
    }

    @Override
    public ProductDetailDTO getProductDetailById(int id) {
        log.info("Fetching product detail with ID: {}", id);

        return productDetailRepository.findById(id)
                .map(productDetail -> {
                    log.info("Found product detail with ID: {}", id);
                    return productDetailMapper.toDto(productDetail);
                })
                .orElseGet(() -> {
                    log.warn("Product detail with ID {} not found", id);
                    return null;
                });
    }

    @Override
    public ProductDetailDTO getProductDetailByProductIdAndColorAndSize(int productId, Color color, Size size) {
        log.info("Fetching product detail for Product ID: {}, Color: {}, Size: {}", productId, color, size);

        ProductDetail productDetail = productDetailRepository.findOneByColorSizeAndProductId(productId, color, size);
        if (productDetail != null) {
            log.info("Found product detail with ID: {}", productDetail.getProductDetailID());
            return productDetailMapper.toDto(productDetail);
        } else {
            log.warn("No product detail found for Product ID: {}, Color: {}, Size: {}", productId, color, size);
            return null;
        }
    }
}
