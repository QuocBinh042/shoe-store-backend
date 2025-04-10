package com.shoestore.Server.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Getter
@Setter
@ToString
public class OrderDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderDetailID;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "orderID")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "productDetailID")
    private ProductDetail productDetail;

    private int quantity;
    private double price;
    @OneToOne(mappedBy = "orderDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Review review;


}