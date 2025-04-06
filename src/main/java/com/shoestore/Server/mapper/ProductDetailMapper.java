package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.dto.request.ProductDetailRequest;
import com.shoestore.Server.dto.response.ProductDetailsResponse;
import com.shoestore.Server.entities.ProductDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {
    ProductDetailDTO toDto(ProductDetail productDetail);
    ProductDetail toEntity(ProductDetailDTO productDetailDTO);

    ProductDetail toEntity(ProductDetailRequest productDetailRequest);
    ProductDetailsResponse toResponse(ProductDetail productDetail);
}
