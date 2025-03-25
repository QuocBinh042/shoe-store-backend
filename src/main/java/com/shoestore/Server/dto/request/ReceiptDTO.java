package com.shoestore.Server.dto.request;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ReceiptDTO {
    private int receiptID;
    private double total;
    private LocalDate receiptDate;
    private OrderDTO order;


}

