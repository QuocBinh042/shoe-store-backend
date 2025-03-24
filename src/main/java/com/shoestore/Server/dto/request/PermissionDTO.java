package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PermissionDTO {
    private int permissionID;

    @NotBlank(message = "Tên quyền không được để trống")
    @Size(max = 100, message = "Tên quyền không được vượt quá 100 ký tự")
    private String name;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;
}
