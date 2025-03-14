package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.CategoryDTO;
import com.shoestore.Server.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDto(Category entity);
    Category toEntity(CategoryDTO dto);
}
