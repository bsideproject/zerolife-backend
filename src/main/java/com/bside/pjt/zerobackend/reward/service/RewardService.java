package com.bside.pjt.zerobackend.reward.service;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.exception.ServiceException;
import com.bside.pjt.zerobackend.mission.repository.MissionProgressRepository;
import com.bside.pjt.zerobackend.reward.domain.AchievedReward;
import com.bside.pjt.zerobackend.reward.domain.Reward;
import com.bside.pjt.zerobackend.reward.repository.AchievedRewardQueryRepository;
import com.bside.pjt.zerobackend.reward.repository.AchievedRewardRepository;
import com.bside.pjt.zerobackend.reward.repository.RewardRepository;
import com.bside.pjt.zerobackend.reward.service.dto.AchievedRewardDto;
import com.bside.pjt.zerobackend.user.domain.User;
import com.bside.pjt.zerobackend.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;
    private final AchievedRewardRepository achievedRewardRepository;
    private final AchievedRewardQueryRepository achievedRewardQueryRepository;
    private final UserRepository userRepository;
    private final MissionProgressRepository missionProgressRepository;

    @Transactional
    public void create(final long userId, final int progressOrder) {
        // 1. 탈퇴한 사용자가 아닌지 확인
        final Optional<User> user = userRepository.findByIdAndDeletedFalse(userId);
        if (user.isEmpty()) {
            log.error(ErrorCode.E1000.getMessage() + " - UserId: " + userId);
        }

        // 2. 사용자가 가장 최근에 받은 리워드 조회
        final Optional<AchievedReward> latest = achievedRewardRepository.findFirstByUserIdOrderByCreatedAtDesc(userId);
        final int nextOrder = latest.isEmpty() ? 1 : latest.get().rewardOrder() + 1;

        // 3. 사용자의 현재 미션 완료 수 계산
        final int totalCompletedMissionCount = missionProgressRepository.countByUserIdAndCompletedTrue(userId);

        // 4. 2번 리워드의 다음 리워드 조회
        final Optional<Reward> next = rewardRepository.findByOrder(nextOrder);
        if (next.isEmpty()) {
            log.error(ErrorCode.E4000.getMessage());
        }

        // 5. 4번 리워드의 달성 기준을 만족하면 달성 리워드 생성
        if (totalCompletedMissionCount >= next.get().getRequirement()) {
            achievedRewardRepository.save(new AchievedReward(user.get(), next.get(), progressOrder));
        }
    }

    @Transactional(readOnly = true)
    public List<AchievedRewardDto> findAllByUserId(final long userId) {
        final User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1000));

        final List<Reward> rewards = rewardRepository.findAll(Sort.by(Direction.ASC, "order"));
        final List<Long> achievedRewardIds = achievedRewardQueryRepository.findAllRewardIdsByUserId(userId);

        return rewards.stream()
            .map(reward -> {
                final boolean isAchieved = achievedRewardIds.contains(reward.getId());
                return AchievedRewardDto.of(reward, isAchieved);})
            .collect(Collectors.toList());
    }

    @Transactional
    public List<AchievedReward> findAllNewAchievedRewards(final long userId) {
        // 1. 탈퇴한 사용자가 아닌지 확인
        final User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1000));

        // 2. 아직 사용자가 확인하지 않은 달성 리워드 조회
        final List<AchievedReward> achievedRewards =  achievedRewardRepository.findAllByUserIdAndCheckedFalse(userId);

        // 3. 각각의 달성 리워드 확인 여부 변경
        achievedRewards.forEach(AchievedReward::checked);

        return achievedRewards;
    }
}
