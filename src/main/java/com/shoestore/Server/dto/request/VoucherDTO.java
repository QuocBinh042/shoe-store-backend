package com.shoestore.Server.dto.request;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class VoucherDTO {
    private int voucherID;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private String discountType;
    private BigDecimal minOrderValue;
    private boolean freeShipping;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean status;


}


