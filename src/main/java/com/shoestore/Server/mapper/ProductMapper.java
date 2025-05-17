package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.ProductDTO;
import com.shoestore.Server.dto.response.SearchProductResponse;
import com.shoestore.Server.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "brand.brandID", target = "brandID")
    @Mapping(source = "category.categoryID", target = "categoryID")
    @Mapping(source = "supplier.supplierID", target = "supplierID")
    ProductDTO toDto(Product product);

    @Mapping(source = "brandID", target = "brand.brandID")
    @Mapping(source = "categoryID", target = "category.categoryID")
    @Mapping(source = "supplierID", target = "supplier.supplierID")
    Product toEntity(ProductDTO productDTO);
    List<SearchProductResponse> toListProductSearchResponse(List<Product> products);
    SearchProductResponse toProductSearchResponse(Product products);
    List<ProductDTO> toDto(List<Product> products);
}
