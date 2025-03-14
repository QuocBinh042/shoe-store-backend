package com.shoestore.Server.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private int userID;
    private String name;
    private String email;
    private String phoneNumber;
    private String userName;
    private String status;
    private String CI;
    private RoleDTO role;
    private String refreshToken;
}
