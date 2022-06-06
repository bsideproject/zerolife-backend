package com.bside.pjt.zerobackend.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceExceptionAdviser {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiError> handleServiceException(ServiceException e) {
        final ApiError body = new ApiError(e.getErrorCode());
        return ResponseEntity.status(e.getStatusCode()).body(body);
    }
}
