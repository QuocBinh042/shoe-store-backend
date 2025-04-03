package com.shoestore.Server.entities;

import com.shoestore.Server.enums.VoucherType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Voucher extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voucherID;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String description;

    private BigDecimal discountValue;

    @Enumerated(EnumType.STRING)
    private VoucherType discountType;

    private BigDecimal minOrderValue;

    private boolean freeShipping;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean status;

    private Integer maxUses;

    private Integer usedCount;

    private String productRestriction;
}
