package com.bside.pjt.zerobackend.user.controller.response;

import com.bside.pjt.zerobackend.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class UserResponse {

    private final String nickname;
}
