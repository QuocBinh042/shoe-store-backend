package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.entities.Color;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.entities.ProductDetail;
import com.shoestore.Server.entities.Size;
import com.shoestore.Server.mapper.ProductDetailMapper;
import com.shoestore.Server.mapper.ProductMapper;
import com.shoestore.Server.repositories.ProductDetailRepository;
import com.shoestore.Server.repositories.ProductRepository;
import com.shoestore.Server.repositories.ReviewRepository;
import com.shoestore.Server.service.PaginationService;
import com.shoestore.Server.service.ProductService;
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

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PaginationService paginationService;
    private final ReviewRepository reviewRepository;
    
    @Override
    public PaginationResponse<ProductDTO> getAllProduct(int page, int pageSize) {
        List<Product> products = productRepository.findAll();

        PaginationResponse<Product> paginatedProducts = paginationService.paginate(products, page, pageSize);
        List<ProductDTO> productDTOs = productMapper.toDto(paginatedProducts.getItems());

        return new PaginationResponse<>(productDTOs, paginatedProducts.getTotalElements(), paginatedProducts.getTotalPages(), paginatedProducts.getCurrentPage(), paginatedProducts.getPageSize());
    }
    @Override
    public ProductDTO getProductByProductDetailsId(int id) {
        Product product = productRepository.findProductByProductDetailId(id);
        return product != null ? productMapper.toDto(product) : null;
    }

    @Override
    public ProductDTO getProductById(int id) {
        Product product = productRepository.findById(id).orElse(null);
        return product != null ? productMapper.toDto(product) : null;
    }

    @Override
    public PaginationResponse<ProductDTO> getFilteredProducts(List<Integer> categoryIds, List<Integer> brandIds, List<String> colors, List<String> sizes,
                                                              String keyword, Double minPrice, Double maxPrice, String sortBy, int page, int pageSize) {
        Specification<Product> spec = Specification.where(null);

        if (categoryIds != null && !categoryIds.isEmpty()) {
            spec = spec.and(ProductSpecification.hasCategories(categoryIds));
        }

        if (brandIds != null && !brandIds.isEmpty()) {
            spec = spec.and(ProductSpecification.hasBrands(brandIds));
        }

        if (colors != null && !colors.isEmpty()) {
            spec = spec.and(ProductSpecification.hasColors(colors));
        }

        if (sizes != null && !sizes.isEmpty()) {
            spec = spec.and(ProductSpecification.hasSizes(sizes));
        }

        if (minPrice != null) {
            spec = spec.and(ProductSpecification.hasMinPrice(minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.hasMaxPrice(maxPrice));
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = spec.and(ProductSpecification.hasName(keyword));
        }

        Sort sort = Sort.unsorted();

        if (sortBy != null) {
            switch (sortBy) {
                case "Price: High-Low":
                    sort = Sort.by(Sort.Order.desc("price"));
                    break;
                case "Price: Low-High":
                    sort = Sort.by(Sort.Order.asc("price"));
                    break;
                case "Newest":
                    sort = Sort.by(Sort.Order.desc("createDate"));
                    break;
            }
            System.out.println("Sắp xếp: " + sortBy);
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

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
                pagedProducts.getNumber() + 1, // chuyển từ 0-based index sang 1-based
                pagedProducts.getSize()
        );
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }
    
    @Override
    @Transactional
    public ProductDTO updateProduct(int id, ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findById(id);
        
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setStatus(productDTO.getStatus());
            Product updatedProduct = productRepository.save(product);
            return productMapper.toDto(updatedProduct);
        }
        
        return null;
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
    public double getAverageRating(int id) {
        return reviewRepository.getAverageRatingByProductId(id)
                .map(avg -> Math.round(avg * 2) / 2.0)
                .orElse(0.0);
    }
}
