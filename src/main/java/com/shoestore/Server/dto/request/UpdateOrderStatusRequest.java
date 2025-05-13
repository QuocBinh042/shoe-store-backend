package com.shoestore.Server.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    private String status;
    private String trackingNumber;
    private String cancelReason;
    private int userId;
}

