package com.shoestore.Server.entities;

import com.shoestore.Server.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "OrderStatusHistory")
public class OrderStatusHistory extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "cancelReason")
    private String cancelReason;

    @Column(name = "trackingNumber")
    private String trackingNumber;

    @Column(name = "deliveredAt")
    private LocalDateTime deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changedBy")
    private User changedBy;

}