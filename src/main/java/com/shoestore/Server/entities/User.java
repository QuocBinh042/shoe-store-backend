package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoestore.Server.enums.CustomerGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private int userID;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(unique = true)
    private String email;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    private String password;
    private String CI;
    private String status;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Address> addresses;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;
    @Column(length = 500)
    private String refreshToken;

    @ManyToMany
    @JoinTable(
            name = "User_Roles",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "roleID")
    )
    @JsonIgnore
    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
    @Column(name = "customerGroup")
    private CustomerGroup customerGroup;
}
