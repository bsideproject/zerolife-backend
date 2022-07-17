package com.bside.pjt.zerobackend.common.security;

import com.bside.pjt.zerobackend.common.ApiError;
import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.util.JsonUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        response.getWriter().write(JsonUtils.toJson(new ApiError(ErrorCode.E0001)));
        response.getWriter().flush();
    }
}
