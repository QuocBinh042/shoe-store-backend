package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.RoleDTO;
import com.shoestore.Server.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    @Mapping(source = "permissions", target = "permissions")
    RoleDTO toDto(Role role);

    @Mapping(source = "permissions", target = "permissions")
    Role toEntity(RoleDTO roleDTO);

    List<RoleDTO> toDtoList(List<Role> roles);
}