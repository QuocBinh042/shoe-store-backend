package com.shoestore.Server.advice;

import com.shoestore.Server.dto.response.RestResponse;
import com.shoestore.Server.utils.annotation.ApiMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        if (body instanceof RestResponse) {
            return body;
        }

        if (body instanceof String) {
            return body;
        }
        String path = request.getURI().getPath();
        if (path.contains("/swagger-ui") || path.contains("/v3/api-docs") || path.contains("/api-docs")) {
            return body;
        }

        if (status >= 400) {
            RestResponse<Object> restResponse = new RestResponse<>();
            restResponse.setStatusCode(status);
            restResponse.setMessage("API ERROR");
            restResponse.setError(body.toString());
            restResponse.setData(null);
            return restResponse;
        }
        else {
            RestResponse<Object> restResponse = new RestResponse<>();
            restResponse.setStatusCode(status);
            restResponse.setData(body);
            ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
            restResponse.setMessage(apiMessage != null ? apiMessage.value() : "CALL API SUCCESS");
            return restResponse;
        }
    }
}
