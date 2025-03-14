package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.entities.OrderDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderMapper.class, ProductDetailMapper.class})
public interface OrderDetailMapper {
    OrderDetailDTO toDto(OrderDetail entity);
    OrderDetail toEntity(OrderDetailDTO dto);
}
