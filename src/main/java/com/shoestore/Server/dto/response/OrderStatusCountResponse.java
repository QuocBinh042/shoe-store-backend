package com.shoestore.Server.dto.response;

import com.shoestore.Server.enums.OrderStatus;

public record OrderStatusCountResponse (
    OrderStatus status,
    long count
){}
