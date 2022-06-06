package com.bside.pjt.zerobackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class ServiceException extends RuntimeException {

    private final int statusCode;
    private final ErrorCode errorCode;
}
