package com.bside.pjt.zerobackend.user.service;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.ServiceException;
import com.bside.pjt.zerobackend.mission.repository.MissionProgressRepository;
import com.bside.pjt.zerobackend.reward.repository.AchievedRewardRepository;
import com.bside.pjt.zerobackend.user.domain.User;
import com.bside.pjt.zerobackend.user.repository.UserRepository;
import com.bside.pjt.zerobackend.user.service.dto.MyPageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MissionProgressRepository missionProgressRepository;
    private final AchievedRewardRepository achievedRewardRepository;

    private final int TOTAL_MISSIONS_COUNT = 5;

    @Transactional(readOnly = true)
    public MyPageDto myPage(final long userId) {
        // 1. 현재 탈퇴하지 않은 사용자인지 확인
        final User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1000));

        // 2. 완료한 미션 수 계산
        final int completedMissionsCount = missionProgressRepository.countByUserIdAndCompletedTrue(userId);

        // 3. 남은 미션 수 계산
        final int rest = missionProgressRepository.countByUserId(userId) % TOTAL_MISSIONS_COUNT;
        final int leftMissionsCount = TOTAL_MISSIONS_COUNT - rest;

        // 4. 달성 리워드 수 계산
        final int achievedRewardsCount = achievedRewardRepository.countByUserId(userId);

        return MyPageDto.builder()
            .user(user)
            .completedMissionsCount(completedMissionsCount)
            .leftMissionsCount(leftMissionsCount)
            .achievedRewardsCount(achievedRewardsCount)
            .build();
    }
}
