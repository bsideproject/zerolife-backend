package com.bside.pjt.zerobackend.common.security;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring()
            .antMatchers("/favicon.ico")
            .antMatchers("/error");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.httpBasic()
            .and()
            .headers().disable()
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
                response.getWriter().write("{code: 403, message: 권한이 없는 요청입니다.}");
                response.getWriter().flush();
            })
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
                response.getWriter().write("{code: 401, message: 인증되지 않은 요청입니다.}");
                response.getWriter().flush();
            })
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/").permitAll()
            .antMatchers(HttpMethod.GET, "/apis/oauth/kakao").permitAll()
            .antMatchers(HttpMethod.GET, "/apis/oauth/redirect").permitAll()
            .antMatchers(HttpMethod.GET, "/apis/daily-mission-progress").permitAll()
            .antMatchers(HttpMethod.POST, "/apis/daily-mission-progress").permitAll()
            .antMatchers(HttpMethod.PUT, "/apis/daily-mission-progress").permitAll()
            .anyRequest().authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        final var configuration = new CorsConfiguration();

        // TODO: 프론트 호스트 주소 확인 후, 수정
        configuration.setAllowedOriginPatterns(List.of(
            "*"
        ));

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(1800L);

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

