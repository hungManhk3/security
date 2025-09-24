package com.example.security.exception;

import com.example.security.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<String>> handlingAppException(AppException e){
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<String>> handlingValidException(MethodArgumentNotValidException e){
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ignored){}

        ApiResponse<String> apiResponse = new ApiResponse<String>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setStatus(errorCode.getStatus());
        return ResponseEntity.status(errorCode.getStatus()).body(apiResponse);
    }
}
