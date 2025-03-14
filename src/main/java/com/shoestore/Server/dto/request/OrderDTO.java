package com.shoestore.Server.dto.request;
import lombok.Data;

import java.time.LocalDate;
@Data
public class OrderDTO {
    private int orderID;
    private LocalDate orderDate;
    private String status;
    private double total;
    private double feeShip;
    private String code;
    private VoucherDTO voucher;
    private String shippingAddress;
    private String typePayment;
    private double discount;
    private UserDTO user;

}
