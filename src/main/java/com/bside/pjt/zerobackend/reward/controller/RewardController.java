package com.bside.pjt.zerobackend.reward.controller;

import com.bside.pjt.zerobackend.reward.controller.response.NewAchievedRewardResponse;
import com.bside.pjt.zerobackend.reward.domain.AchievedReward;
import com.bside.pjt.zerobackend.reward.service.RewardService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping("/apis/achieved-rewards/new")
    public ResponseEntity<List<NewAchievedRewardResponse>> findAllNewAchievedRewards() {

        // TODO: 추후 토큰에서 가져오는 로직 추가
        final Long userId = 1L;
        final List<AchievedReward> result = rewardService.findAllNewAchievedRewards(userId);

        final List<NewAchievedRewardResponse> response = result.stream()
            .map(NewAchievedRewardResponse::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
