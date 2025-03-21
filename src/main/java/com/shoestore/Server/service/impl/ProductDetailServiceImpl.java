package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.entities.ProductDetail;

import com.shoestore.Server.enums.Color;
import com.shoestore.Server.enums.Size;
import com.shoestore.Server.mapper.ProductDetailMapper;
import com.shoestore.Server.mapper.ProductMapper;
import com.shoestore.Server.repositories.ProductDetailRepository;
import com.shoestore.Server.repositories.ProductRepository;
import com.shoestore.Server.service.ProductDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailMapper productDetailMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDetailServiceImpl(ProductDetailRepository productDetailRepository, ProductDetailMapper productDetailMapper, ProductRepository productRepository, ProductMapper productMapper) {
        this.productDetailRepository = productDetailRepository;
        this.productDetailMapper = productDetailMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
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


    @Override
    @Transactional
    public ProductDetailDTO createProductDetail(int productId, ProductDetailDTO productDetailDTO) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            ProductDetail productDetail = productDetailMapper.toEntity(productDetailDTO);
            productDetail.setProduct(productOpt.get());

            ProductDetail savedDetail = productDetailRepository.save(productDetail);
            return productDetailMapper.toDto(savedDetail);
        }

        return null;
    }

    @Override
    @Transactional
    public ProductDetailDTO updateProductDetail(int productDetailId, ProductDetailDTO productDetailDTO) {
        Optional<ProductDetail> existingDetail = productDetailRepository.findById(productDetailId);

        if (existingDetail.isPresent()) {
            ProductDetail detail = existingDetail.get();

            detail.setColor(Color.valueOf(productDetailDTO.getColor()));
            detail.setSize(Size.valueOf(productDetailDTO.getSize()));
            detail.setStockQuantity(productDetailDTO.getStockQuantity());

            ProductDetail updatedDetail = productDetailRepository.save(detail);
            return productDetailMapper.toDto(updatedDetail);
        }

        return null;
    }
}
