package com.bside.pjt.zerobackend.user.controller.response;

import com.bside.pjt.zerobackend.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class UserResponse {

    private final String profileImageUrl;
    private final String nickname;

    public static UserResponse from(final User user) {
        return new UserResponse(user.getProfileImageUrl(), user.getNickname());
    }
}
