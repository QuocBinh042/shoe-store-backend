package com.shoestore.Server.specifications;

import com.shoestore.Server.entities.Product;
import com.shoestore.Server.entities.ProductDetail;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {

    public static Specification<Product> hasCategories(List<Integer> categoryIds) {
        return (root, query, builder) -> categoryIds == null || categoryIds.isEmpty() ?
                null : root.get("category").get("categoryID").in(categoryIds);
    }


    public static Specification<Product> hasBrands(List<Integer> brandIds) {
        return (root, query, builder) -> brandIds == null || brandIds.isEmpty() ?
                null : root.get("brand").get("brandID").in(brandIds);
    }

    public static Specification<Product> hasColors(List<String> colors) {
        return (root, query, builder) -> colors == null || colors.isEmpty() ?
                null : root.get("productDetails").get("color").in(colors);
    }

    public static Specification<Product> hasSizes(List<String> sizes) {
        return (root, query, builder) -> sizes == null || sizes.isEmpty() ?
                null : root.get("productDetails").get("size").in(sizes);
    }

    public static Specification<Product> hasMinPrice(Double minPrice) {
        return (root, query, builder) -> minPrice == null ? null : builder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> hasMaxPrice(Double maxPrice) {
        return (root, query, builder) -> maxPrice == null ? null : builder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
    public static Specification<Product> hasName(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }
            return builder.like(builder.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Product> hasSuppliers(List<Integer> supplierIds) {
        return (root, query, builder) ->
                (supplierIds == null || supplierIds.isEmpty()) ? null : root.get("supplier").get("supplierID").in(supplierIds);
    }

    public static Specification<Product> hasStockStatus(String stockStatus) {
        return (root, query, builder) -> {
            if (stockStatus == null || stockStatus.trim().isEmpty()) {
                return null;
            }
            // Thực hiện join với productDetails để lấy giá trị stockQuantity
            Join<Product, ProductDetail> productDetailsJoin = root.join("productDetails");
            if ("in_stock".equalsIgnoreCase(stockStatus)) {
                return builder.greaterThan(productDetailsJoin.get("stockQuantity"), 0);
            } else if ("out_of_stock".equalsIgnoreCase(stockStatus)) {
                return builder.lessThan(productDetailsJoin.get("stockQuantity"), 0);
            }
            return null;
        };
    }
}
