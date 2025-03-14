package com.shoestore.Server.mapper;


import com.shoestore.Server.dto.request.CartDTO;
import com.shoestore.Server.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "user.userID", target = "userID")
    CartDTO toDto(Cart entity);

    @Mapping(source = "userID", target = "user.userID")
    Cart toEntity(CartDTO dto);
}
