package com.bside.pjt.zerobackend.user.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class LoginResponse {

    private final Long id;
    private final String accessToken;
}
