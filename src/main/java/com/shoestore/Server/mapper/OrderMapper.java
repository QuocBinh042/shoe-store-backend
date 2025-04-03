package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EnumMapper.class})
public interface OrderMapper {
    OrderDTO toDto(Order entity);
    Order toEntity(OrderDTO dto);
}
