package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.SignUpRequest;
import com.shoestore.Server.dto.request.UserDTO;
import com.shoestore.Server.entities.User;
import com.shoestore.Server.dto.request.RoleDTO; // Add RoleDTO import
import com.shoestore.Server.entities.Role; // Add Role import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    @Mapping(source = "roles", target = "roles")
    UserDTO toDto(User entity);
    @Mapping(source = "roles", target = "roles")
    User toEntity(UserDTO dto);
//    @Mapping(source = "roles", target = "roles")
//    UserDTO toDto(User entity);
    User toSignUpRequest(SignUpRequest signUpRequest);
}
