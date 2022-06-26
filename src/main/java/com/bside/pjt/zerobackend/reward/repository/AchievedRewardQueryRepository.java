package com.bside.pjt.zerobackend.reward.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AchievedRewardQueryRepository {

    private final EntityManager em;

    public List<Long> findAllRewardIdsByUserId(final long userId) {
        final String query = "SELECT a.reward_id FROM AchievedReward a WHERE a.user_id = :userId";
        return em.createQuery(query)
            .setParameter("userId", userId)
            .getResultList();
    }
}
