package com.bside.pjt.zerobackend.common.security;

import com.bside.pjt.zerobackend.common.security.jwt.JwtFilter;
import com.bside.pjt.zerobackend.common.security.jwt.JwtResolver;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
            .accessDeniedHandler(accessDeniedHandler())
            .authenticationEntryPoint(authenticationEntryPoint())

            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/").permitAll()
            .antMatchers(HttpMethod.POST, "/apis/users").permitAll()
            // TODO: 인증 기능 구현 완료 후, permitAll() 목록에서 제거
            .antMatchers(HttpMethod.GET, "/apis/daily-mission-progress").permitAll()
            .antMatchers(HttpMethod.POST, "/apis/mission-progress").permitAll()
            .antMatchers(HttpMethod.PUT, "/apis/mission-progress/*").permitAll()
            .antMatchers(HttpMethod.GET, "/apis/mission-progress").permitAll()
            .antMatchers(HttpMethod.GET, "/apis/users/mypage").permitAll()
            .antMatchers(HttpMethod.GET, "/apis/users/completed-missions").permitAll()
            .antMatchers(HttpMethod.GET, "/apis/users/achieved-rewards").permitAll()
            .antMatchers(HttpMethod.GET, "/apis/achieved-rewards/new").permitAll()
            .anyRequest().authenticated()

            .and()
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
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

    @Bean
    public SecurityAccessDeniedHandler accessDeniedHandler() {
        return new SecurityAccessDeniedHandler();
    }

    @Bean
    public SecurityAuthenticationEntryPoint authenticationEntryPoint() {
        return new SecurityAuthenticationEntryPoint();
    }

    @Bean
    public JwtResolver jwtResolver() {
        return new JwtResolver();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtResolver());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

