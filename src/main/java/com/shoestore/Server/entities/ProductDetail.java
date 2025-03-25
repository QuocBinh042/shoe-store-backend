package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoestore.Server.enums.Color;
import com.shoestore.Server.enums.Size;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.util.List;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productDetailID")
    private int productDetailID;
    @Enumerated(EnumType.STRING)
    private Color color;
    @Enumerated(EnumType.STRING)
    private Size size;
    @Column(name = "stockQuantity", nullable = false)
    @DecimalMin(value = "0", inclusive = false, message = "Stock quantity must be greater than 0")
    private int stockQuantity;
    @ManyToOne
    @JoinColumn(name = "productID")
    @JsonBackReference("productDetailsReference")
    private Product product;
    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CartItem> cartItems;
    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<OrderDetail> orderDetails;

}
