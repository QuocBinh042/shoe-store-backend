package com.shoestore.Server.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table
public class Receipt extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receiptID")
    private int receiptID;

    private double total;
    private LocalDate receiptDate;

    @OneToOne
    @JoinColumn(name = "orderID")
    private Order order;
}
