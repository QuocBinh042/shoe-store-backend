package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.PromotionDTO;
import com.shoestore.Server.dto.response.PromotionResponse;
import com.shoestore.Server.entities.Category;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.entities.Promotion;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    @Mapping(source = "giftProduct.productID", target = "giftProductID")
    @Mapping(source = "categories", target = "categoryIDs", qualifiedByName = "toCategoryIds")
    @Mapping(source = "applicableProducts", target = "applicableProductIDs", qualifiedByName = "toProductIds")
    PromotionResponse toResponse(Promotion promotion);

    @Named("toCategoryIds")
    default List<Integer> toCategoryIds(List<Category> categories) {
        return categories.stream().map(Category::getCategoryID).collect(Collectors.toList());
    }

    @Named("toProductIds")
    default List<Integer> toProductIds(List<Product> products) {
        return products.stream().map(Product::getProductID).collect(Collectors.toList());
    }

    PromotionDTO toDto(Promotion promotion);
    Promotion toEntity(PromotionDTO promotionDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PromotionDTO dto, @MappingTarget Promotion entity);

}
