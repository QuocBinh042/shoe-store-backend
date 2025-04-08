package com.shoestore.Server.dto.request;

import com.shoestore.Server.enums.CustomerGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private int userID;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\d{10,15}$", message = "Phone number must be between 10 and 15 digits")
    private String phoneNumber;

//    @NotBlank(message = "Status cannot be blank")
    private String status;

//    @NotNull(message = "Roles cannot be null")
    private Set<RoleDTO> roles;

    private String refreshToken;

    private CustomerGroup customerGroup;
}
