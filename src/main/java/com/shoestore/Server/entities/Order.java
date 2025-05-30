package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoestore.Server.enums.OrderStatus;
import com.shoestore.Server.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Orders")
@ToString
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderID")
    private int orderID;
    private LocalDate orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private double total;
    private double feeShip;
    private String code;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "voucherID", nullable = true)
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

    private String shippingMethod;

    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private double voucherDiscount;
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private Receipt receipt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> statusHistory;

}
