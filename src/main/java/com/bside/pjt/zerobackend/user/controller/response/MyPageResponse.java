package com.bside.pjt.zerobackend.user.controller.response;

import com.bside.pjt.zerobackend.user.service.dto.MyPageDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class MyPageResponse {

    private final UserResponse user;
    private final MissionStateResponse missionState;

    public static MyPageResponse from(final MyPageDto dto) {
        return new MyPageResponse(
            new UserResponse(dto.getUser().getNickname()),
            new MissionStateResponse(
                dto.getCompletedMissionsCount(),
                dto.getLeftMissionsCount(),
                dto.getAchievedRewardsCount()
            )
        );
    }
}
