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
        final String query = "SELECT a.reward.id FROM AchievedReward a WHERE a.user.id = :userId";
        return em.createQuery(query, Long.class)
            .setParameter("userId", userId)
            .getResultList();
    }
}
