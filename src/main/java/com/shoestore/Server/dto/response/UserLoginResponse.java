package com.shoestore.Server.dto.response;

import com.shoestore.Server.dto.request.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    private int id;
    private String email;
    private String phoneNumber;
    private String name;
    private RoleDTO role;
}
