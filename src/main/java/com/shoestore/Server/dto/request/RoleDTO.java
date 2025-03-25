package com.shoestore.Server.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RoleDTO {
    private int roleID;
    private String roleType;
    private String description;
    private List<PermissionDTO> permissions;
}
