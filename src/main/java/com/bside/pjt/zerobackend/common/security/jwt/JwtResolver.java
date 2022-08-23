package com.bside.pjt.zerobackend.common.security.jwt;

import com.bside.pjt.zerobackend.common.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

@Slf4j
public class JwtResolver implements InitializingBean {

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-expired-days}")
    private int accessTokenExpiredDays;

    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        final byte[] decoded = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decoded);
    }

    public String parseToken(final HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || authorization.isBlank()) {
            log.error(ErrorCode.E0003.getMessage());
            return null;
        }

        if (!authorization.startsWith("Bearer")) {
            log.error(ErrorCode.E0004.getMessage());
            return null;
        }

        return authorization.substring(7);
    }

    public boolean validateToken(final String token) {
        try {
            final JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
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

    public JwtAuthentication getAuthentication(final String token) {
        final JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build();
        final Claims claims = parser.parseClaimsJws(token).getBody();

        final Long id = claims.get("id", Long.class);
        final String email = claims.get("email", String.class);
        final String nickname = claims.get("nickname", String.class);
        final String type = claims.get("type", String.class);
        if (id == null || id == 0) {
            log.error(ErrorCode.E1002.getMessage());
        }
        if (email == null || email.isBlank()) {
            log.error(ErrorCode.E1003.getMessage());
        }
        if (nickname == null || nickname.isBlank()) {
            log.error(ErrorCode.E1004.getMessage());
        }
        if (type == null || type.isBlank()) {
            log.error(ErrorCode.E1004.getMessage());
        }

        final JwtPrincipal principal = new JwtPrincipal(id, email, nickname, type);
        return new JwtAuthentication(null, principal, null);
    }

    public String issueAccessToken(final JwtPrincipal principal) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", SignatureAlgorithm.HS512.name());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", principal.getId());
        claims.put("email", principal.getEmail());
        claims.put("nickname", principal.getNickname());
        claims.put("type", principal.getType());

        return Jwts.builder()
            .setHeader(headers)
            .setClaims(claims)
            .setIssuer(issuer)
            .setExpiration(Date.from(ZonedDateTime.now().plusDays(accessTokenExpiredDays).toInstant()))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }
}
