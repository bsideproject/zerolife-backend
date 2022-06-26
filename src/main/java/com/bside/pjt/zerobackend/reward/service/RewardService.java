package com.bside.pjt.zerobackend.reward.service;

import com.bside.pjt.zerobackend.reward.domain.Reward;
import com.bside.pjt.zerobackend.reward.repository.AchievedRewardQueryRepository;
import com.bside.pjt.zerobackend.reward.repository.RewardRepository;
import com.bside.pjt.zerobackend.reward.service.dto.AchievedRewardDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;
    private final AchievedRewardQueryRepository achievedRewardQueryRepository;

    public List<AchievedRewardDto> findAchievedRewards(final long userId) {
        final List<Reward> rewards = rewardRepository.findAll();
        final List<Long> achievedRewardIds = achievedRewardQueryRepository.findAllRewardIdsByUserId(userId);

        return rewards.stream()
            .map(reward -> {
                final boolean isAchieved = achievedRewardIds.contains(reward.getId());
                return AchievedRewardDto.of(reward, isAchieved);})
            .collect(Collectors.toList());
    }
}
