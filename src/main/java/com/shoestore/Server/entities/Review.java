package com.shoestore.Server.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewID")
    private int reviewID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productDetailID")
    private ProductDetail productDetail;

    @OneToOne
    @JoinColumn(name = "orderID", nullable = false, unique = true)
    private Order order;

    private int rating;

    @Column(name = "comment", nullable = false)
    private String comment;

}
