package com.bside.pjt.zerobackend.common.oauth.config;

import com.bside.pjt.zerobackend.common.oauth.Provider;
import com.bside.pjt.zerobackend.common.oauth.Registration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class Oauth2ClientProperties {

    private final Map<String, Provider> provider = new HashMap<>();

    private final Map<String, Registration> registration = new HashMap<>();

    public Map<String, Provider> getProvider() {
        return this.provider;
    }

    public Map<String, Registration> getRegistration() {
        return this.registration;
    }

}
