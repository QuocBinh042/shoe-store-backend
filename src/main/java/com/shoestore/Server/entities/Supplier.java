package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table
@Getter
@Setter
public class Supplier extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplierID")
    private int supplierID;
    private String supplierName;
    private String address;
    @OneToMany(mappedBy = "supplier")
    @JsonBackReference("supplierReference")
    private List<Product> products;
    @ElementCollection
    @CollectionTable(name = "Supplier_PhoneNumber", joinColumns = @JoinColumn(name = "supplierID"))
    @Column(name = "phoneNumbers")
    private List<String> phoneNumbers;
}
