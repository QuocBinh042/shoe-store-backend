package com.shoestore.Server.mapper;
import com.shoestore.Server.dto.request.OrderHistoryStatusDTO;
import com.shoestore.Server.entities.OrderStatusHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderStatusHistoryMapper {
    OrderStatusHistory toEntity(OrderHistoryStatusDTO dto);

}
