package com.bside.pjt.zerobackend.common.exception;

import com.bside.pjt.zerobackend.common.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class ServiceException extends RuntimeException {

    private final int statusCode;
    private final ErrorCode errorCode;
}
