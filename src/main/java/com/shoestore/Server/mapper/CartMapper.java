package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.CartDTO;
import com.shoestore.Server.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDTO toDto(Cart entity);
    Cart toEntity(CartDTO dto);
}
