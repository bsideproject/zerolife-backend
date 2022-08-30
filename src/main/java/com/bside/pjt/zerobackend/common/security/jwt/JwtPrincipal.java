package com.bside.pjt.zerobackend.common.security.jwt;

import com.bside.pjt.zerobackend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPrincipal {

    private Long id;
    private String email;
    private String nickname;
    private String type;

    public JwtPrincipal(final User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.type = user.getType();
    }
}
