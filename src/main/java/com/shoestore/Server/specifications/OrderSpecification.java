package com.shoestore.Server.specifications;

import com.shoestore.Server.entities.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Slf4j
public class OrderSpecification {

    public static Specification<Order> hasStatus(String status) {
        return (root, query, builder) ->
                (status == null || status.trim().isEmpty()) ? null : builder.equal(root.get("status"), status);
    }

    public static Specification<Order> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.trim().isEmpty()) return null;
            return builder.or(
                    builder.like(builder.lower(root.get("code")), "%" + keyword.toLowerCase() + "%"),
                    builder.like(builder.lower(root.get("user").get("name")), "%" + keyword.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Order> hasDateFrom(LocalDate from) {
        return (root, query, builder) ->
                from == null ? null : builder.greaterThanOrEqualTo(root.get("orderDate"), from);
    }

    public static Specification<Order> hasDateTo(LocalDate to) {
        return (root, query, builder) ->
                to == null ? null : builder.lessThanOrEqualTo(root.get("orderDate"), to);
    }

}
