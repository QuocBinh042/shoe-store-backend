package com.shoestore.Server.dto.response;

import lombok.Getter;

@Getter
public enum ApiStatusResponse {
    SUCCESS(200, "Success"),
    CREATED(201, "Created successfully"),
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Resource not found"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    UNAUTHORIZED(401, "Unauthorized access"),
    FORBIDDEN(403, "Forbidden request");

    private final int code;
    private final String message;

    ApiStatusResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
