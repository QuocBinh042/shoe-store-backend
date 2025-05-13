package com.shoestore.Server.dto.request;

import com.shoestore.Server.entities.Order;
import com.shoestore.Server.entities.User;
import com.shoestore.Server.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class OrderHistoryStatusDTO {
    private Order order;
    private OrderStatus status;

    private String cancelReason;

    private String trackingNumber;

    private LocalDateTime deliveredAt;

    private User changedBy;
}
