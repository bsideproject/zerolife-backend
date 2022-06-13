package com.bside.pjt.zerobackend.common.oauth.service;

import com.bside.pjt.zerobackend.common.oauth.ClientRegistration;
import com.bside.pjt.zerobackend.common.oauth.Oauth2Token;
import com.bside.pjt.zerobackend.common.oauth.domain.Oauth2UserInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Oauth2Service {

    public void redirectAuthorizePage(ClientRegistration clientRegistration, String state, HttpServletResponse response) throws IOException;

    public Oauth2Token getAccessToken(ClientRegistration clientRegistration, String code);

    public Oauth2UserInfo getUserInfo(ClientRegistration clientRegistration, String accessToken);

}
