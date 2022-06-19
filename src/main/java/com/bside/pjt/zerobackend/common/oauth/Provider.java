package com.bside.pjt.zerobackend.common.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Provider {

    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
}
