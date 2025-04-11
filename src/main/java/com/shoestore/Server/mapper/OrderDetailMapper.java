package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.OrderDetailDTO;
import com.shoestore.Server.dto.response.OrderDetailResponse;
import com.shoestore.Server.dto.response.PlacedOrderDetailsResponse;
import com.shoestore.Server.entities.OrderDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderMapper.class, ProductDetailMapper.class})
public interface OrderDetailMapper {
    OrderDetailDTO toDto(OrderDetail entity);
    OrderDetail toEntity(OrderDetailDTO dto);
    List<PlacedOrderDetailsResponse> toListDto(List<OrderDetail> entity);
    OrderDetailResponse toDResponse(OrderDetail entity);

}
