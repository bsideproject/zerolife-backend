package com.bside.pjt.zerobackend.common.oauth;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Oauth2Token {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiredAt;

    public Oauth2Token() {

    }

    public Oauth2Token(String accessToken, LocalDateTime expiredAt) {
        this.accessToken = accessToken;
        this.expiredAt = expiredAt;
    }
}
