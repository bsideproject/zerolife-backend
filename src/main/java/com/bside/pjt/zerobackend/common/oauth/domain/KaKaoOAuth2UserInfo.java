package com.bside.pjt.zerobackend.common.oauth.domain;

import java.math.BigDecimal;
import java.util.Map;

public class KaKaoOAuth2UserInfo extends Oauth2UserInfo {

    public KaKaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return new BigDecimal(String.valueOf(attributes.get("id"))).toEngineeringString();
    }

    @Override
    public String getName() {
        return (String) parsingProfile().get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) parsingKakaoAccount().get("email");
    }

    @Override
    public String getProfileImage() {
        return (String) parsingProperties().get("profile_image");
    }

    private Map<String, Object> parsingProperties() {
        return (Map<String, Object>) attributes.get("properties");
    }

    private Map<String, Object> parsingKakaoAccount() {
        return (Map<String, Object>) attributes.get("kakao_account");
    }

    private Map<String, Object> parsingProfile() {
        return (Map<String, Object>) parsingKakaoAccount().get("profile");
    }
}
