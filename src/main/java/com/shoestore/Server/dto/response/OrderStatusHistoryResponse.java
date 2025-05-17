package com.shoestore.Server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistoryResponse {
    private int orderID;
    private String status;
    private LocalDateTime changedAt;
    private String trackingNumber;
    private String cancelReason;
    private int changedById;
    private String changedByName;
}
