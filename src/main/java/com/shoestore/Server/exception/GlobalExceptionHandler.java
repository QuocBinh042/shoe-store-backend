package com.shoestore.Server.exception;

import com.shoestore.Server.dto.response.RestResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 1) Handle lỗi VALIDATION của @Valid @RequestBody
     *    (MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<Map<String, String>> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            Map<String, String> errorObj = new HashMap<>();
            errorObj.put("field", fieldError.getField());
            errorObj.put("error", fieldError.getDefaultMessage());
            errors.add(errorObj);
        }

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("Validation Error");
        res.setError("Validation Error");
        res.setData(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * 2) Handle ConstraintViolationException
     *    (thường gặp khi @Valid áp dụng ở Entity hoặc @PathVariable, @RequestParam)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<Map<String, String>> errors = new ArrayList<>();

        ex.getConstraintViolations().forEach(violation -> {
            Map<String, String> errorObj = new HashMap<>();
            errorObj.put("field", violation.getPropertyPath().toString());
            errorObj.put("error", violation.getMessage());
            errors.add(errorObj);
        });

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("Validation Error");
        res.setError("Validation Error");
        res.setData(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }


    /**
     * 3) Handle DataIntegrityViolationException
     *    (vi phạm constraint DB: unique, foreign key, ...)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RestResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.CONFLICT.value());
        res.setMessage("Data integrity violation");
        res.setError("Conflict");
        res.setData(null);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    /**
     * 4) Handle TransactionSystemException
     *    (thường bọc ConstraintViolationException bên trong)
     */
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<RestResponse<Object>> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof ConstraintViolationException cve) {
            return handleConstraintViolationException(cve);
        }
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage("Transaction error");
        res.setError("Internal Server Error");
        res.setData(null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    /**
     * 5) Handle HttpMessageNotReadableException
     *    (JSON parse error, sai format JSON,...)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("JSON parse error or malformed request body");
        res.setError("Bad Request");
        res.setData(null);

        return ResponseEntity.badRequest().body(res);
    }

    /**
     * 6) Handle MissingServletRequestParameterException
     *    (thiếu request param bắt buộc)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<RestResponse<Object>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("Missing required parameter: " + ex.getParameterName());
        res.setError("Bad Request");
        res.setData(null);

        return ResponseEntity.badRequest().body(res);
    }

    /**
     * 7) Handle NoHandlerFoundException
     *    (khi URL không match với bất kỳ controller nào)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());
        res.setError("Not Found");
        res.setData(null);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * 8) Handle AccessDeniedException (Spring Security)
     *    (user không đủ quyền truy cập)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setMessage("Access Denied");
        res.setError("Forbidden");
        res.setData(null);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    /**
     * 9) Catch-all cho mọi Exception còn lại
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleAllExceptions(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        res.setError("Internal Server Error");
        res.setData(null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<RestResponse<Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.CONFLICT.value());
        res.setMessage(ex.getMessage());
        res.setError("Conflict");
        res.setData(null);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }
    @ExceptionHandler(UserNotActiveException.class)
    public ResponseEntity<RestResponse<Object>> handleUserNotActiveException(UserNotActiveException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RestResponse<>(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), "Unauthorized", null));
    }


}

