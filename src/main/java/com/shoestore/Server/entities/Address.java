package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Address")
@ToString
public class Address extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressID")
    private int addressID;

    @Column(name = "street", nullable = false, columnDefinition = "VARCHAR(100)")
    private String street;

    @Column(name = "city", nullable = false, columnDefinition = "VARCHAR(50)")
    private String city;

    @Column(name = "ward", nullable = false, columnDefinition = "VARCHAR(50)")
    private String ward;

    @Column(name = "district", nullable = false, columnDefinition = "VARCHAR(250)")
    private String district;

    @Column(name = "fullName", nullable = false, columnDefinition = "VARCHAR(100)")
    private String fullName;

    @Column(name = "phone", nullable = false, columnDefinition = "VARCHAR(10)")
    private String phone;

    @Column(columnDefinition = "VARCHAR(20)")
    private String type;

    @Column(name = "isDefault", nullable = false)
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "userID")
    @JsonBackReference
    private User user;
}