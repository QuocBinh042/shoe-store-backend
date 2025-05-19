package com.shoestore.Server.dto.response;

public record CustomerGrowthResponse(
        String month,
        Long newCustomers,
        Long returningCustomers
) { }
