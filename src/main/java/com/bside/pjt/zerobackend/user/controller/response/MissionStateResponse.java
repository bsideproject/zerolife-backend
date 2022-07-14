package com.bside.pjt.zerobackend.user.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class MissionStateResponse {

    private final Integer completedMissionsCount;
    private final Integer leftMissionsCount;
    private final Integer achievedRewardsCount;
}
