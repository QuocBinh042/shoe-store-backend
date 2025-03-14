package com.shoestore.Server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;
    private UserLoginResponse user;
}
