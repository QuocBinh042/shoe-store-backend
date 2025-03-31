package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "voucher", source = "voucher")
    OrderDTO toDto(Order entity);
    @Mapping(target = "user", source = "user")
    @Mapping(target = "voucher", source = "voucher")
    Order toEntity(OrderDTO dto);
}
