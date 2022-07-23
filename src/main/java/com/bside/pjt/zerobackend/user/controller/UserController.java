package com.bside.pjt.zerobackend.user.controller;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.exception.ServiceException;
import com.bside.pjt.zerobackend.common.security.jwt.JwtPrincipal;
import com.bside.pjt.zerobackend.mission.service.MissionService;
import com.bside.pjt.zerobackend.mission.service.dto.CompletedDailyMissionDto;
import com.bside.pjt.zerobackend.reward.service.RewardService;
import com.bside.pjt.zerobackend.reward.service.dto.AchievedRewardDto;
import com.bside.pjt.zerobackend.user.controller.response.MyPageResponse;
import com.bside.pjt.zerobackend.user.service.UserService;
import com.bside.pjt.zerobackend.user.service.dto.MyPageDto;
import java.util.List;
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
public class UserController {

    private final UserService userService;
    private final MissionService missionService;
    private final RewardService rewardService;

    @GetMapping("apis/users/mypage")
    public ResponseEntity<MyPageResponse> myPage(
        @AuthenticationPrincipal final JwtPrincipal principal
    ) {
        final Long userId = principal.getId();
        if (userId == null || userId == 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1002);
        }

        final MyPageDto result = userService.myPage(userId);

        return ResponseEntity.ok(MyPageResponse.from(result));
    }

    @GetMapping("apis/users/completed-missions")
    public ResponseEntity<List<CompletedDailyMissionDto>> findCompletedMissions(
        @AuthenticationPrincipal final JwtPrincipal principal
    ) {
        final Long userId = principal.getId();
        if (userId == null || userId == 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1002);
        }

        final List<CompletedDailyMissionDto> result = missionService.findCompletedMissionProgressList(userId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("apis/users/achieved-rewards")
    public ResponseEntity<List<AchievedRewardDto>> findAchievedRewards(
        @AuthenticationPrincipal final JwtPrincipal principal
    ) {
        final Long userId = principal.getId();
        if (userId == null || userId == 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1002);
        }

        final List<AchievedRewardDto> result = rewardService.findAllByUserId(userId);

        return ResponseEntity.ok(result);
    }
}
