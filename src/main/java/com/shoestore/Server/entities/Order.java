package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name ="Orders")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderID")
    private int orderID;
    private LocalDate orderDate;
    private String status;
    private double total;
    private double feeShip;
    private String code;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "voucherID",nullable = true)
    private Voucher voucher;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Column(nullable = true)
    @JsonIgnore
    private List<OrderDetail> orderDetails;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private Payment payment;
    @Column(name = "shippingAddress", nullable = false)
    private String shippingAddress;
    private String typePayment;
    private double discount;
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Review review;


}
