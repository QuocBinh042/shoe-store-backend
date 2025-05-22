package com.shoestore.Server.dto.request;

import lombok.Data;

@Data
public class OrderCancelRequest {
    private int orderId;
    private int userId;
    private String reason;
}
