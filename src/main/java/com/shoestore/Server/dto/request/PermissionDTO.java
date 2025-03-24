package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PermissionDTO {
    private int permissionID;

    @NotBlank(message = "Permission name cannot be blank")
    @Size(max = 100, message = "Permission name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}
