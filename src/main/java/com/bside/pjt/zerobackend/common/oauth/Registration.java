package com.bside.pjt.zerobackend.common.oauth;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Registration {

    private String clientId;
    private String clientSecret;
    private String authorizationGrantType;
    private String redirectUri;
    private Set<String> scope;

}
