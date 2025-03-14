package com.shoestore.Server.dto.request;
import lombok.Data;

@Data
public class AddressDTO {
    private int addressID;
    private String street;
    private String city;
    private String ward;
    private String district;
    private String fullName;
    private String phone;
    private String type;
    private boolean isDefault;
    private UserDTO user;
}

