package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.SupplierDTO;
import com.shoestore.Server.entities.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierDTO toDto(Supplier supplier);
    Supplier toEntity(SupplierDTO supplierDTO);
}

