package com.bside.pjt.zerobackend.reward.repository;

import com.bside.pjt.zerobackend.reward.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward, Long> {

}
