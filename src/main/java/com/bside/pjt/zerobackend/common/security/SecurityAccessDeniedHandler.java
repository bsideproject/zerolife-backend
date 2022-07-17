package com.bside.pjt.zerobackend.common.security;

import com.bside.pjt.zerobackend.common.ApiError;
import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.util.JsonUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        response.getWriter().write(JsonUtils.toJson(new ApiError(ErrorCode.E0002)));
        response.getWriter().flush();
    }
}
