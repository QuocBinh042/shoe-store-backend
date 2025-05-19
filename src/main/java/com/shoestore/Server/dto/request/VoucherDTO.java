package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VoucherDTO {
    private int voucherID;

    @NotBlank(message = "Voucher code cannot be blank")
    @Size(max = 50, message = "Voucher code must not exceed 50 characters")
    private String code;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Discount value cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount value must be greater than 0")
    private BigDecimal discountValue;

    @NotBlank(message = "Discount type cannot be blank")
    @Pattern(regexp = "^(PERCENT|FIXED)$", message = "Discount type must be either PERCENT or FIXED")
    private String discountType;

    @NotNull(message = "Minimum order value cannot be null")
    @DecimalMin(value = "0.0", message = "Minimum order value cannot be negative")
    private BigDecimal minOrderValue;

    private Integer maxUses;

    private boolean freeShipping;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDateTime endDate;

    private boolean status;

    private Integer usedCount;

    private String productRestriction;
}
