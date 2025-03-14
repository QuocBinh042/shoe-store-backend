package com.shoestore.Server.dto.request;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class PromotionDTO {
    private int promotionID;
    private String name;
    private String description;
    private BigDecimal discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean status;

}

