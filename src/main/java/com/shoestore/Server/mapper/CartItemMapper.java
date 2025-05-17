package com.shoestore.Server.mapper;


import com.shoestore.Server.dto.request.CartItemDTO;
import com.shoestore.Server.entities.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {CartMapper.class, ProductDetailMapper.class})
public interface CartItemMapper {
    CartItemDTO toCartItemDTO(CartItem entity);
    CartItem toEntity(CartItemDTO dto);
}

