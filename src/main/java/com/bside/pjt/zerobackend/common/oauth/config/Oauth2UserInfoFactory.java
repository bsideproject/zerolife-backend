package com.bside.pjt.zerobackend.common.oauth.config;

import com.bside.pjt.zerobackend.common.oauth.domain.KaKaoOAuth2UserInfo;
import com.bside.pjt.zerobackend.common.oauth.domain.Oauth2UserInfo;

import java.util.Map;

public class Oauth2UserInfoFactory {

    public static Oauth2UserInfo getUserInfo(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return new KaKaoOAuth2UserInfo(attributes);
        } else {
            throw new IllegalArgumentException(registrationId.toUpperCase() + "로그인은 지원하지 않습니다.");
        }
    }

}
