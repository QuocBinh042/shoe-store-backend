package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.PromotionDTO;
import com.shoestore.Server.entities.Promotion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    PromotionDTO toDto(Promotion promotion);
    Promotion toEntity(PromotionDTO promotionDTO);
}
