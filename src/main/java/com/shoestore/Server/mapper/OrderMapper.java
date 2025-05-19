package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.response.OrderResponse;
import com.shoestore.Server.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EnumMapper.class})
public interface OrderMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "voucher", source = "voucher")
    OrderDTO toDto(Order entity);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "voucher", source = "voucher")
    Order toEntity(OrderDTO dto);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "voucher", source = "voucher")
    OrderResponse toResponse(Order entity);
}
