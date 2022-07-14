package com.bside.pjt.zerobackend.user.service.dto;

import com.bside.pjt.zerobackend.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class MyPageDto {

    private final User user;
    private final Integer completedMissionsCount;
    private final Integer leftMissionsCount;
    private final Integer achievedRewardsCount;
}
