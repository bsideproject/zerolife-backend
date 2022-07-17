package com.bside.pjt.zerobackend.user.controller.request;

import com.bside.pjt.zerobackend.common.validation.NickName;
import com.bside.pjt.zerobackend.common.validation.Password;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class SignUpRequest {

    @NotNull(message = "E1003")
    @Email(message = "E1005")
    private final String email;

    @NickName
    private final String nickname;

    @Password
    private final String password;

    @NotNull(message = "E1012")
    private final Boolean marketingAgreement;
}
