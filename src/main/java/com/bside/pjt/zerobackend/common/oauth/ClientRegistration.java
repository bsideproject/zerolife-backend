package com.bside.pjt.zerobackend.common.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ClientRegistration {

    private String registrationId;

    private Registration registration;

    private Provider provider;

    @Builder
    public ClientRegistration(String registrationId, Registration registration, Provider provider) {
        this.registrationId = registrationId;
        this.registration = registration;
        this.provider = provider;
    }

}
