package com.shoestore.Server.dto.request;

import lombok.Data;

import java.util.List;
@Data
public class SupplierDTO {
    private int supplierID;
    private String supplierName;
    private String address;
    private List<String> phoneNumbers;

}

