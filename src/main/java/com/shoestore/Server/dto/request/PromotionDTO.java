package com.shoestore.Server.dto.request;

import com.shoestore.Server.enums.ApplicableTo;
import com.shoestore.Server.enums.CustomerGroup;
import com.shoestore.Server.enums.PromotionStatus;
import com.shoestore.Server.enums.PromotionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PromotionDTO {
    private int promotionID;

    @NotBlank(message = "Promotion name cannot be blank")
    @Size(max = 100, message = "Promotion name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Promotion type is required")
    private PromotionType type;

    @NotNull(message = "Discount value cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount value must be greater than 0")
    private BigDecimal discountValue;

    private Integer buyQuantity;
    private Integer getQuantity;

    private Integer giftProductID;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    private BigDecimal minOrderValue;
    private BigDecimal maxDiscount;

    private ApplicableTo applicableTo;

    private List<Integer> categoryIDs;

    private List<Integer> applicableProductIDs;

    @NotEmpty(message = "Customer groups cannot be empty")
    private List<CustomerGroup> customerGroups;

    private Boolean useCode;
    private String code;

    private PromotionStatus status;
    private Boolean stackable;
    private Integer usageLimit;

    private String image;
    private Integer usageCount;
}
