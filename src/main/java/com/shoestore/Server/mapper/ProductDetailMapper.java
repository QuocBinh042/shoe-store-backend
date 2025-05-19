package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.request.ProductDetailRequest;
import com.shoestore.Server.dto.response.ProductDetailsResponse;
import com.shoestore.Server.entities.ProductDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {
    ProductDetailsResponse toProductDetailsResponse(ProductDetail productDetail);
    ProductDetail toEntity(ProductDetailsResponse productDetailDTO);

    ProductDetail toEntity(ProductDetailRequest productDetailRequest);
    ProductDetailsResponse toResponse(ProductDetail productDetail);
}
