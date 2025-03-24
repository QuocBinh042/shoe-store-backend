package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ProductDTO;

import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.dto.response.ProductSearchResponse;

import com.shoestore.Server.entities.Product;

import com.shoestore.Server.mapper.ProductMapper;
import com.shoestore.Server.repositories.ProductRepository;
import com.shoestore.Server.repositories.ReviewRepository;
import com.shoestore.Server.service.PaginationService;
import com.shoestore.Server.service.ProductService;
import com.shoestore.Server.service.PromotionService;
import com.shoestore.Server.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PaginationService paginationService;
    private final ReviewRepository reviewRepository;
    private final PromotionService promotionService;

    private List<ProductSearchResponse> enhanceProductSearchResponses(List<ProductSearchResponse> products) {
        for (ProductSearchResponse p : products) {
            p.setRating(getAverageRating(p.getProductID()));
            p.setDiscountPrice(promotionService.getDiscountedPrice(p.getProductID()));
        }
        return products;
    }

    @Override
    public PaginationResponse<ProductSearchResponse> getAllProduct(int page, int pageSize) {
        List<Product> products = productRepository.findAll();

        PaginationResponse<Product> paginatedProducts = paginationService.paginate(products, page, pageSize);
        List<ProductSearchResponse> productDTOs = productMapper.toProductSearchResponse(paginatedProducts.getItems());

        return new PaginationResponse<>(
                enhanceProductSearchResponses(productDTOs),
                paginatedProducts.getTotalElements(),
                paginatedProducts.getTotalPages(),
                paginatedProducts.getCurrentPage(),
                paginatedProducts.getPageSize()
        );
    }

    @Override
    public PaginationResponse<ProductSearchResponse> getFilteredProducts(List<Integer> categoryIds, List<Integer> brandIds, List<String> colors, List<String> sizes,
                                                                         String keyword, Double minPrice, Double maxPrice, String sortBy, int page, int pageSize) {
        Specification<Product> spec = buildProductSpecification(categoryIds, brandIds, colors, sizes, keyword, minPrice, maxPrice);
        Pageable pageable = PageRequest.of(page - 1, pageSize, getSortOrder(sortBy));
        Page<Product> pagedProducts = productRepository.findAll(spec, pageable);

        List<ProductSearchResponse> productDTOs = productMapper.toProductSearchResponse(pagedProducts.getContent());
        return new PaginationResponse<>(
                enhanceProductSearchResponses(productDTOs),
                pagedProducts.getTotalElements(),
                pagedProducts.getTotalPages(),
                pagedProducts.getNumber() + 1,
                pagedProducts.getSize()
        );
    }

    @Override
    public PaginationResponse<ProductDTO> searchProducts(String status,
                                                         List<Integer> categoryIds,
                                                         List<Integer> brandIds,
                                                         List<Integer> supplierIds,
                                                         String searchText,
                                                         String stock,
                                                         int page,
                                                         int pageSize) {
        Specification<Product> spec = Specification.where(null);

        if (status != null && !status.isEmpty()) {
            spec = spec.and(ProductSpecification.hasStatus(status));
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {
            spec = spec.and(ProductSpecification.hasCategories(categoryIds));
        }

        if (brandIds != null && !brandIds.isEmpty()) {
            spec = spec.and(ProductSpecification.hasBrands(brandIds));
        }

        if (supplierIds != null && !supplierIds.isEmpty()) {
            spec = spec.and(ProductSpecification.hasSuppliers(supplierIds));
        }

        if (searchText != null && !searchText.trim().isEmpty()) {
            spec = spec.and(ProductSpecification.hasName(searchText));
        }

        if (stock != null && !stock.trim().isEmpty()) {
            spec = spec.and(ProductSpecification.hasStockStatus(stock));
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<Product> pagedProducts = productRepository.findAll(spec, pageable);

        List<ProductDTO> productDTOs = productMapper.toDto(pagedProducts.getContent());

        return new PaginationResponse<>(
                productDTOs,
                pagedProducts.getTotalElements(),
                pagedProducts.getTotalPages(),
                pagedProducts.getNumber() + 1,
                pagedProducts.getSize()
        );
    }

    private Specification<Product> buildProductSpecification(List<Integer> categoryIds, List<Integer> brandIds, List<String> colors, List<String> sizes,
                                                             String keyword, Double minPrice, Double maxPrice) {
        Specification<Product> spec = Specification.where(null);

        if (categoryIds != null && !categoryIds.isEmpty()) spec = spec.and(ProductSpecification.hasCategories(categoryIds));
        if (brandIds != null && !brandIds.isEmpty()) spec = spec.and(ProductSpecification.hasBrands(brandIds));
        if (colors != null && !colors.isEmpty()) spec = spec.and(ProductSpecification.hasColors(colors));
        if (sizes != null && !sizes.isEmpty()) spec = spec.and(ProductSpecification.hasSizes(sizes));
        if (minPrice != null) spec = spec.and(ProductSpecification.hasMinPrice(minPrice));
        if (maxPrice != null) spec = spec.and(ProductSpecification.hasMaxPrice(maxPrice));
        if (keyword != null && !keyword.trim().isEmpty()) spec = spec.and(ProductSpecification.hasName(keyword));

        return spec;
    }

    private Sort getSortOrder(String sortBy) {
        if (sortBy == null) return Sort.unsorted();
        return switch (sortBy) {
            case "Price: High-Low" -> Sort.by(Sort.Order.desc("price"));
            case "Price: Low-High" -> Sort.by(Sort.Order.asc("price"));
            case "Newest" -> Sort.by(Sort.Order.desc("createDate"));
            default -> Sort.unsorted();
        };
    }

    public double getAverageRating(int id) {
        return reviewRepository.getAverageRatingByProductId(id)
                .map(avg -> Math.round(avg * 2) / 2.0)
                .orElse(0.0);
    }

    @Override
    public ProductDTO getProductByProductDetailsId(int id) {
        Product product = productRepository.findProductByProductDetailId(id);
        return product != null ? productMapper.toDto(product) : null;
    }

    @Override
    public ProductDTO getProductById(int id) {
        return productRepository.findById(id).map(productMapper::toDto).orElse(null);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDTO)));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(int id, ProductDTO productDTO) {
        return productRepository.findById(id).map(product -> {
            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setStatus(productDTO.getStatus());
            return productMapper.toDto(productRepository.save(product));
        }).orElse(null);
    }

    
    @Override
    @Transactional
    public boolean deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<ProductSearchResponse> getRelatedProducts(int productId, int categoryId, int brandId) {
        List<Product> relatedProducts = productRepository.findTop10ByCategory_CategoryIDAndProductIDNot(categoryId, productId);

        if (relatedProducts.size() < 10) {
            List<Product> brandProducts = productRepository.findTop10ByBrand_BrandIDAndProductIDNot(brandId, productId)
                    .stream()
                    .filter(p -> !relatedProducts.contains(p))
                    .toList();
            relatedProducts.addAll(brandProducts);
        }
        List<ProductSearchResponse> productSearchResponse=productMapper.toProductSearchResponse(relatedProducts.stream().limit(10).collect(Collectors.toList()));
        return enhanceProductSearchResponses(productSearchResponse);
    }
}
