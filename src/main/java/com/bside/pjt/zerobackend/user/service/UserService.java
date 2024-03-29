package com.bside.pjt.zerobackend.user.service;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.exception.ServiceException;
import com.bside.pjt.zerobackend.mission.domain.MissionProgress;
import com.bside.pjt.zerobackend.mission.repository.MissionProgressRepository;
import com.bside.pjt.zerobackend.reward.domain.AchievedReward;
import com.bside.pjt.zerobackend.reward.repository.AchievedRewardRepository;
import com.bside.pjt.zerobackend.user.controller.request.LoginRequest;
import com.bside.pjt.zerobackend.user.controller.request.SignUpRequest;
import com.bside.pjt.zerobackend.user.domain.User;
import com.bside.pjt.zerobackend.user.repository.UserRepository;
import com.bside.pjt.zerobackend.user.service.dto.MyPageDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MissionProgressRepository missionProgressRepository;
    private final AchievedRewardRepository achievedRewardRepository;
    private final PasswordEncoder passwordEncoder;

    private final int TOTAL_MISSIONS_COUNT = 5;

    @Transactional
    public void create(final SignUpRequest request) {
        if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1013);
        }

        if (userRepository.existsByNicknameAndDeletedFalse(request.getNickname())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1014);
        }

        final String encodedPassword = passwordEncoder.encode(request.getPassword());

        final User user = new User(
            request.getEmail(),
            request.getNickname(),
            encodedPassword,
            request.getMarketingAgreement()
        );

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(final LoginRequest request) {
        final User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1000));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), ErrorCode.E1015);
        }

        return user;
    }

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

    @Transactional
    public void delete(final long userId) {
        // 1. 현재 탈퇴하지 않은 사용자인지 확인
        final User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1001));

        // 2. 달성 리워드 삭제
        final List<AchievedReward> achievedRewards = achievedRewardRepository.findAllByUserId(userId);
        achievedRewards.forEach(AchievedReward::delete);

        // 3. 데일리 미션 삭제
        final List<MissionProgress> missionProgressList = missionProgressRepository.findAllByUserId(userId);
        missionProgressList.forEach(MissionProgress::delete);

        // 5. 유저 삭제
        user.delete();
    }
}
