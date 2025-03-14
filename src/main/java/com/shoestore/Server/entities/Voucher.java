package com.shoestore.Server.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Voucher")
public class Voucher extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voucherID;
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    @Column(name = "description", nullable = false)
    private String description;

    private BigDecimal discountValue;

    private String discountType;

    private BigDecimal minOrderValue;

    private boolean freeShipping;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean status;
}
