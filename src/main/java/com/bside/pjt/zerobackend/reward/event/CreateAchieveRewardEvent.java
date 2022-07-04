package com.bside.pjt.zerobackend.reward.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class CreateAchieveRewardEvent {

    private final long userId;
    private final int progressOrder;
}
