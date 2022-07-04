package com.bside.pjt.zerobackend.reward.event;

import com.bside.pjt.zerobackend.reward.service.RewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RewardEventHandler {

    private final RewardService rewardService;

    @Async
    @EventListener
    public void achieve(CreateAchieveRewardEvent event) {
        rewardService.create(event.getUserId(), event.getProgressOrder());
    }
}
