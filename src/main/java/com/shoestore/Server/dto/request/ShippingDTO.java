package com.shoestore.Server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShippingDTO {
    @NotBlank(message = "Shipping address cannot be blank")
    private String shippingAddress;

    private String shippingMethod;

    private String trackingNumber;
}