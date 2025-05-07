package com.shoestore.Server.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequest {
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

}
