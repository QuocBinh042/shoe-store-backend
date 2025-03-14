package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.RoleDTO;
import com.shoestore.Server.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDto(Role role);
    Role toEntity(RoleDTO roleDTO);
}