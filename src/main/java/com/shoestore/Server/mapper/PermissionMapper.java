package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.PermissionDTO;
import com.shoestore.Server.entities.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionDTO toDto(Permission permission);
    Permission toEntity(PermissionDTO permissionDTO);
}
