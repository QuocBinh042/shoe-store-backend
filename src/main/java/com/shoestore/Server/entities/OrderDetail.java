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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giftProductDetailID", nullable = true)
    private ProductDetail giftProductDetail;
    private int quantity;
    private double price;
    @OneToOne(mappedBy = "orderDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Review review;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotionID", nullable = true)
    private Promotion promotion;
    private int giftedQuantity;
    private Double promoDiscount;
}