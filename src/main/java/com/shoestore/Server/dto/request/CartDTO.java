package com.shoestore.Server.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDTO {
    private int cartID;

    @NotNull(message = "Thông tin người dùng không được để trống")
    private UserDTO user;
}
