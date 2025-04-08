package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.RoleDTO;
import com.shoestore.Server.enums.CustomerGroup;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {
    private int userID;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    private String status;

    private Set<RoleDTO> roles;

    private String refreshToken;

    private CustomerGroup customerGroup;
}
