package com.bside.pjt.zerobackend.reward.service.dto;

import com.bside.pjt.zerobackend.reward.domain.Reward;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class AchievedRewardDto {

    private final Long id;
    private final Boolean isAchieved;

    public static AchievedRewardDto of(final Reward reward, final boolean isAchieved) {
        return new AchievedRewardDto(
            reward.getId(),
            isAchieved
        );
    }
}
