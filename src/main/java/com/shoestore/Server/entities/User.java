package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
//@ToString
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private int userID;
    @Column(name = "name", nullable = false)
    private String name;
    private String email;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Address> addresses;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    private String password;
    @Column(name = "userName")
    private String userName;
    private String CI;
    private String status;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    @JsonIgnore
    private List<Order> orders;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
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
}
