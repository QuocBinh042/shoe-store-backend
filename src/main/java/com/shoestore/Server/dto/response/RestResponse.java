package com.shoestore.Server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RestResponse<T> {
    private int statusCode;
    private Object message;
    private String error;
    private T data;
}
