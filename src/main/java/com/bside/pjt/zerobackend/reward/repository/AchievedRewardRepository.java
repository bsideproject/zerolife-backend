package com.bside.pjt.zerobackend.reward.repository;

import com.bside.pjt.zerobackend.reward.domain.AchievedReward;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievedRewardRepository extends JpaRepository<AchievedReward, Long> {

    Optional<AchievedReward> findFirstByUserIdOrderByCreatedAtDesc(final long userId);

    List<AchievedReward> findAllByUserIdAndCheckedFalse(final long userId);
}
