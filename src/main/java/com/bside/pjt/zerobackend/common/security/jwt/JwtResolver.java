package com.bside.pjt.zerobackend.common.security.jwt;

import com.bside.pjt.zerobackend.common.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

@Slf4j
public class JwtResolver {

    @Value("jwt.issuer")
    private String issuer;
    @Value("jwt.secret-key")
    private String secretKey;

    public String parseToken(final HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || authorization.isBlank()) {
            log.error(ErrorCode.E0003.getMessage());
            return null;
        }

        if (authorization.startsWith("Bearer")) {
            log.error(ErrorCode.E0004.getMessage());
            return null;
        }

        return authorization.substring(7);
    }

    public boolean validateToken(final String token) {
        try {
            final JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .build();
            parser.parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error(ErrorCode.E0005.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error(ErrorCode.E0006.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error(ErrorCode.E0007.getMessage());
            return false;
        } catch (SignatureException e) {
            log.error(ErrorCode.E0008.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(final String token) {
        final JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
            .build();
        final Claims claims = parser.parseClaimsJws(token).getBody();

        final Long id = claims.get("id", Long.class);
        final String email = claims.get("email", String.class);
        final String nickname = claims.get("nickname", String.class);
        if (id == null || id == 0) {
            log.error(ErrorCode.E1002.getMessage());
        }
        if (email == null || email.isBlank()) {
            log.error(ErrorCode.E1003.getMessage());
        }
        if (nickname == null || nickname.isBlank()) {
            log.error(ErrorCode.E1004.getMessage());
        }

        final JwtPrincipal principal = new JwtPrincipal(id, email, nickname);
        return new JwtAuthentication(null, principal, null);
    }
}
