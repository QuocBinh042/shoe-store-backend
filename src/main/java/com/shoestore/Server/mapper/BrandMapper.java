package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.BrandDTO;
import com.shoestore.Server.dto.request.SupplierDTO;
import com.shoestore.Server.entities.Brand;
import com.shoestore.Server.entities.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandDTO toDto(Brand brand);
    Brand toEntity(BrandDTO brandDTO);
}
