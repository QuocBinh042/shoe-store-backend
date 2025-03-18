package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@ToString
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewID")
    private int reviewID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productID")
    @JsonIgnore
    private Product product;

    @OneToOne
    @JoinColumn(name = "orderDetailID", nullable = false, unique = true)
    private OrderDetail orderDetail;
    @Min(1)
    @Max(5)
    private int rating;

    @Column(name = "comment", nullable = true)
    private String comment;


}
