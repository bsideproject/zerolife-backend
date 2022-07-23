package com.bside.pjt.zerobackend.common.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtResolver jwtResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        // 1. 헤더에서 토큰 가져오기
        final String token = jwtResolver.parseToken((HttpServletRequest) request);
        // 2. 토큰 유효성 검증하기
        if (token != null && jwtResolver.validateToken(token)) {
            // 3. 토큰이 유효한 경우, 토큰에서 Authentication 얻어오기
            final JwtAuthentication authentication = jwtResolver.getAuthentication(token);
            // 4. Authentication을 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
