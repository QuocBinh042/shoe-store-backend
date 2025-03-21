package com.shoestore.Server.dto.request;

import lombok.Data;

@Data
public class PermissionDTO {
    private int permissionID;
    private String name;
    private String description;
}
