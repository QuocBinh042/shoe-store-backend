package com.shoestore.Server.dto.request;

import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
public class UserDTO {
    private int userID;
    private String name;
    private String email;
    private String phoneNumber;
    private String userName;
    private String status;
    private String CI;
    private Set<RoleDTO> roles;
    private String refreshToken;
}
