package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shoestore.Server.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Table
@Entity
@Getter
@Setter
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productID")
    private int productID;

    @Column(name = "productName", nullable = false)
    @NotBlank(message = "Product name must not be empty")
    @Size(max = 50, message = "Product name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Product name may only contain letters, numbers, and spaces")
    private String productName;

    @Column(name = "description", nullable = false, length = 100)
    @NotBlank(message = "Description must not be empty")
    @Size(max = 100, message = "Description must not exceed 100 characters")
    private String description;

    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0", inclusive = false, message = "Price must be greater than 0")
    private double price;

    @Enumerated (EnumType.STRING)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brandID")
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryID")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplierID")
    private Supplier supplier;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference("productDetailsReference")
    private List<ProductDetail> productDetails;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "promotionID", nullable = true)
    @JsonIgnore
    private Promotion promotion;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;

}
