package com.bside.pjt.zerobackend.user.controller.request;

import com.bside.pjt.zerobackend.common.validation.Password;
import com.bside.pjt.zerobackend.common.validation.ValidProvider;
import com.bside.pjt.zerobackend.user.domain.Provider;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class LoginRequest {

    @ValidProvider
    private final Provider provider;

    @NotNull(message = "E1003")
    @Email(message = "E1005")
    private final String email;

    @Password
    private final String password;
}
