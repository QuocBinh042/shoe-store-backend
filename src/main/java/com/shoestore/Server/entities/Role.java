package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoestore.Server.enums.RoleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType roleType;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "Role_Permissions",
            joinColumns = @JoinColumn(name = "roleID"),
            inverseJoinColumns = @JoinColumn(name = "permissionID")
    )
    @JsonIgnore
    private Set<Permission> permissions;
}
