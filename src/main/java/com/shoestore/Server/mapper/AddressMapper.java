package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.AddressDTO;
import com.shoestore.Server.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AddressMapper {
    AddressDTO toDto(Address entity);
    Address toEntity(AddressDTO dto);
}

