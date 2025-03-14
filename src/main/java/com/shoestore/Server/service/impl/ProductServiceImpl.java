package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.response.PaginationResponse;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.mapper.ProductMapper;
import com.shoestore.Server.repositories.ProductRepository;
import com.shoestore.Server.service.PaginationService;
import com.shoestore.Server.service.ProductService;
import com.shoestore.Server.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Autowired
    private final ProductMapper productMapper;
    private final PaginationService paginationService;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, PaginationService paginationService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.paginationService = paginationService;
    }
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


}
