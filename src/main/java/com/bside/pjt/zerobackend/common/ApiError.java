package com.bside.pjt.zerobackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public final class ApiError {

    private final Body error;

    public ApiError(final ErrorCode errorCode) {
        this.error = new Body(errorCode, errorCode.getMessage());
    }

    @Getter
    @RequiredArgsConstructor
    private static class Body {
        private final ErrorCode code;
        private final String message;
    }
}