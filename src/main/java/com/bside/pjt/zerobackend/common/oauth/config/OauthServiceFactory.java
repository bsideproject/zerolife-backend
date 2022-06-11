package com.bside.pjt.zerobackend.common.oauth.config;

import com.bside.pjt.zerobackend.common.oauth.service.KakaoOauth2Service;
import com.bside.pjt.zerobackend.common.oauth.service.Oauth2Service;
import org.springframework.web.client.RestTemplate;

public class OauthServiceFactory {

    public static Oauth2Service getOauth2Service(RestTemplate restTemplate, String registrationId) {

        if ("kakao".equals(registrationId)) {
            return new KakaoOauth2Service(restTemplate);
        } else {
            throw new IllegalArgumentException(registrationId.toUpperCase() + "로그인은 지원하지 않습니다.");
        }
    }

}
