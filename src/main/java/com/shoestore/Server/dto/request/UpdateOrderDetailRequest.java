package com.shoestore.Server.dto.request;

import com.shoestore.Server.enums.Color;
import com.shoestore.Server.enums.Size;
import lombok.Data;

@Data
public class UpdateOrderDetailRequest {
    private Color color;
    private Size size;
    private int quantity;
}

