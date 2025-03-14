package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.ProductDetailDTO;
import com.shoestore.Server.entities.ProductDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {
    ProductDetailDTO toDto(ProductDetail productDetail);
    ProductDetail toEntity(ProductDetailDTO productDetailDTO);
}
