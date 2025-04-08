package com.shoestore.Server.specifications;

import com.shoestore.Server.entities.Promotion;
import com.shoestore.Server.enums.PromotionStatus;
import com.shoestore.Server.enums.PromotionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PromotionSpecification {

    public static Specification<Promotion> hasStatus(PromotionStatus status) {
        return (root, query, builder) -> status == null ? null : builder.equal(root.get("status"), status);
    }

    public static Specification<Promotion> hasType(PromotionType type) {
        return (root, query, builder) -> type == null ? null : builder.equal(root.get("type"), type);
    }

    public static Specification<Promotion> hasName(String name) {
        return (root, query, builder) -> {
            if (name == null || name.trim().isEmpty()) {
                return null;
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Promotion> inDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, builder) -> {
            if (startDate == null || endDate == null) {
                return null;
            }
            return builder.and(
                    builder.lessThanOrEqualTo(root.get("startDate"), endDate),
                    builder.greaterThanOrEqualTo(root.get("endDate"), startDate)
            );
        };
    }
}