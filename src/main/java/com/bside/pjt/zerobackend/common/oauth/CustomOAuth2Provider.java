package com.bside.pjt.zerobackend.common.oauth;

public enum CustomOAuth2Provider {
    KAKAO;

    public ClientRegistration.ClientRegistrationBuilder getBuilder(String registrationId) {
        return ClientRegistration.builder().registrationId(registrationId);
    }
}
