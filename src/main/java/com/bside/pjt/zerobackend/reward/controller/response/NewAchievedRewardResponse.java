package com.bside.pjt.zerobackend.reward.controller.response;

import com.bside.pjt.zerobackend.reward.domain.AchievedReward;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class NewAchievedRewardResponse {

    private final Long achievedRewardId;
    private final String rewardName;
    private final Integer rewardRequirement;

    public static NewAchievedRewardResponse from(final AchievedReward achievedReward) {
        return new NewAchievedRewardResponse(
            achievedReward.getId(),
            achievedReward.rewardName(),
            achievedReward.rewardRequirement()
        );
    }
}
