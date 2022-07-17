package com.bside.pjt.zerobackend.common.security.jwt;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private final transient Object principal;
    private final Object credentials;

    public JwtAuthentication(final Collection<? extends GrantedAuthority> authorities, final Object principal, final Object credentials) {
        super(authorities);

        this.principal = principal;
        this.credentials = credentials;

        super.setAuthenticated(true);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Object getDetails() {
        return super.getDetails();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public boolean isAuthenticated() {
        return super.isAuthenticated();
    }
}
