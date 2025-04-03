package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.response.OrderDetailsResponse;
import com.shoestore.Server.entities.OrderDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderMapper.class, ProductDetailMapper.class})
public interface OrderDetailMapper {
    OrderDetailDTO toDto(OrderDetail entity);
    OrderDetail toEntity(OrderDetailDTO dto);
    List<OrderDetailsResponse> toListDto(List<OrderDetail> entity);
}
