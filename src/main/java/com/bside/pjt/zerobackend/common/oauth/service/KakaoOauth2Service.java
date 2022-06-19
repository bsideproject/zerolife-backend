package com.bside.pjt.zerobackend.common.oauth.service;

import com.bside.pjt.zerobackend.common.oauth.ClientRegistration;
import com.bside.pjt.zerobackend.common.oauth.Oauth2Token;
import com.bside.pjt.zerobackend.common.oauth.config.Oauth2UserInfoFactory;
import com.bside.pjt.zerobackend.common.oauth.domain.Oauth2UserInfo;
import com.bside.pjt.zerobackend.common.util.JsonUtils;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class KakaoOauth2Service implements Oauth2Service {

    public RestTemplate restTemplate;

    public KakaoOauth2Service(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void redirectAuthorizePage(ClientRegistration clientRegistration, String state, HttpServletResponse response) throws IOException {
        String authorizationUri = UriComponentsBuilder.fromUriString(clientRegistration.getProvider().getAuthorizationUri())
                .queryParam("client_id", clientRegistration.getRegistration().getClientId())
                .queryParam("redirect_uri", clientRegistration.getRegistration().getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("state", state)
                .build().encode(StandardCharsets.UTF_8).toUriString();

        log.info("authorizationUri : {}", authorizationUri);
    }

    @Override
    public Oauth2Token getAccessToken(ClientRegistration clientRegistration, String code) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", clientRegistration.getRegistration().getAuthorizationGrantType());
        params.add("client_id", clientRegistration.getRegistration().getClientId());
        params.add("client_secret", clientRegistration.getRegistration().getClientSecret());
        params.add("redirect_uri", clientRegistration.getRegistration().getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> entity = null;

        try {
            entity = restTemplate.exchange(clientRegistration.getProvider().getTokenUri(), HttpMethod.POST, httpEntity, String.class);
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            log.error("statusCode : {}", statusCode);
        }

        log.debug(entity.getBody());

        JsonObject jsonObject = JsonUtils.parse(entity.getBody()).getAsJsonObject();

        String accessToken = jsonObject.get("access_token").getAsString();
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(jsonObject.get("expires_in").getAsLong());

        return new Oauth2Token(accessToken, expiredAt);
    }

    @Override
    public Oauth2UserInfo getUserInfo(ClientRegistration clientRegistration, String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(clientRegistration.getProvider().getUserInfoUri(), HttpMethod.GET, httpEntity, String.class);
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            log.error("statusCode : {}", statusCode);
        }

        log.info(responseEntity.getBody());

        Map<String, Object> userAttributes = JsonUtils.fromJson(responseEntity.getBody(), Map.class);

        Oauth2UserInfo oauth2UserInfo = Oauth2UserInfoFactory.getUserInfo(clientRegistration.getRegistrationId(), userAttributes);

        log.info("id : {}", oauth2UserInfo.getId());
        log.info("name : {}", oauth2UserInfo.getName());
        log.info("email : {}", oauth2UserInfo.getEmail());
        log.info("profile_image : {}", oauth2UserInfo.getProfileImage());

        return oauth2UserInfo;
    }
}
