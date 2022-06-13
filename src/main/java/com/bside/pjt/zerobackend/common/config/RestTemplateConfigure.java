package com.bside.pjt.zerobackend.common.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfigure {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        HttpClient httpClient = HttpClientBuilder
                .create()
                .setMaxConnTotal(100)       //최대 오픈 커넥션 수 제한
                .setMaxConnPerRoute(30)     //ip:port 쌍에 대한 연결 수 제한
                .evictIdleConnections(2000L, TimeUnit.MICROSECONDS)
                //서버에서 keep-alive 시간동안 미사용된 idle 커넥션을 죽였을 때 httpClient에서 인식하지 못해 오류가 발생하는 것을 막기 위해 idle 상태의 커넥션을 주기적으로 지워준다.
                .build();

        factory.setReadTimeout(5000);       //읽기시간초과, ms
        factory.setConnectTimeout(3000);    //연결시간초과, ms
        factory.setHttpClient(httpClient);  //동기실행에 사용될 HttpClient 세팅

        return new RestTemplate(factory);
    }
}
