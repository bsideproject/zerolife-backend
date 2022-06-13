package com.bside.pjt.zerobackend.common.oauth.config;

import com.bside.pjt.zerobackend.common.oauth.ClientRegistration;
import com.bside.pjt.zerobackend.common.oauth.ClientRegistrationRepository;
import com.bside.pjt.zerobackend.common.oauth.CustomOAuth2Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Oauth2Configurer {

    private final Oauth2ClientProperties oauth2ClientProperties;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = oauth2ClientProperties.getRegistration().keySet().stream()
                .map(c -> getRegistration(c))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());

        return new ClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(String client) {
        log.info("kakao : {}", oauth2ClientProperties.getRegistration().get("kakao"));
        log.info("client : {}", client);
        if (client.equals("kakao")) {
            return CustomOAuth2Provider.KAKAO.getBuilder(client)
                    .registration(oauth2ClientProperties.getRegistration().get("kakao"))
                    .provider(oauth2ClientProperties.getProvider().get("kakao"))
                    .build();
        }

        return null;

    }

}
