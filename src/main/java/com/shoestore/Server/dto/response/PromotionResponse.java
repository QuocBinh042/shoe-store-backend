package com.shoestore.Server.dto.response;

import com.shoestore.Server.enums.ApplicableTo;
import com.shoestore.Server.enums.CustomerGroup;
import com.shoestore.Server.enums.PromotionStatus;
import com.shoestore.Server.enums.PromotionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PromotionResponse {
    private int promotionID;
    private String name;
    private String description;
    private PromotionType type;
    private BigDecimal discountValue;
    private Integer buyQuantity;
    private Integer getQuantity;
    private Integer giftProductID;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> timeRanges;
    private BigDecimal minOrderValue;
    private BigDecimal maxDiscount;
    private ApplicableTo applicableTo;
    private List<Integer> categoryIDs;
    private List<Integer> applicableProductIDs;
    private CustomerGroup customerGroup;
    private Boolean useCode;
    private String code;
    private PromotionStatus status;
    private Boolean stackable;
    private Integer usageLimit;
    private String image;
    private Integer usageCount;
}
