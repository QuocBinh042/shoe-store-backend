package com.shoestore.Server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInsideTokenResponse {
    private int id;
    private String email;
    private String phoneNumber;
    private String name;
}
