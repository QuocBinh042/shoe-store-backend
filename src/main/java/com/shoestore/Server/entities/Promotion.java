package com.shoestore.Server.entities;

import com.shoestore.Server.enums.ApplicableTo;
import com.shoestore.Server.enums.CustomerGroup;
import com.shoestore.Server.enums.PromotionStatus;
import com.shoestore.Server.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table
public class Promotion extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int promotionID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PromotionType type;

    private BigDecimal discountValue;

    private Integer buyQuantity;

    private Integer getQuantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gift_product_id")
    private Product giftProduct;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BigDecimal maxDiscount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicableTo applicableTo;

    @ManyToMany
    @JoinTable(
            name = "promotion_categories",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "promotion_products",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> applicableProducts;

    @ElementCollection
    @CollectionTable(name = "promotion_customer_groups", joinColumns = @JoinColumn(name = "promotion_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_group")
    private List<CustomerGroup> customerGroups;

    private Boolean useCode;

    private String code;

    @Enumerated(EnumType.STRING)
    private PromotionStatus status;

    private Boolean stackable;

    private Integer usageLimit;

    private String image;

    private Integer usageCount;

}

