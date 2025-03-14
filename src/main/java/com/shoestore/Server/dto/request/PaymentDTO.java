package com.shoestore.Server.dto.request;


import lombok.Data;

import java.time.LocalDate;
@Data
public class PaymentDTO {
    private int paymentID;
    private OrderDTO order;
    private LocalDate paymentDate;
    private String status;
}

