package com.bside.pjt.zerobackend.reward.controller;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.exception.ServiceException;
import com.bside.pjt.zerobackend.common.security.jwt.JwtPrincipal;
import com.bside.pjt.zerobackend.reward.controller.response.NewAchievedRewardResponse;
import com.bside.pjt.zerobackend.reward.domain.AchievedReward;
import com.bside.pjt.zerobackend.reward.service.RewardService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping("/apis/achieved-rewards/new")
    public ResponseEntity<List<NewAchievedRewardResponse>> findAllNewAchievedRewards(
        @AuthenticationPrincipal final JwtPrincipal principal
    ) {
        final Long userId = principal.getId();
        if (userId == null || userId == 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1002);
        }

        final List<AchievedReward> result = rewardService.findAllNewAchievedRewards(userId);

        final List<NewAchievedRewardResponse> response = result.stream()
            .map(NewAchievedRewardResponse::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
