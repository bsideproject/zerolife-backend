package com.bside.pjt.zerobackend.common.exception;

import com.bside.pjt.zerobackend.common.ApiError;
import com.bside.pjt.zerobackend.common.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiError> handleServiceException(ServiceException e) {
        final ApiError body = new ApiError(e.getErrorCode());
        return ResponseEntity.status(e.getStatusCode()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInvalidArgumentException(MethodArgumentNotValidException e) {
        final ErrorCode errorCode = ErrorCode.valueOf(e.getFieldErrors().get(0).getDefaultMessage());
        final ApiError body = new ApiError(errorCode);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
