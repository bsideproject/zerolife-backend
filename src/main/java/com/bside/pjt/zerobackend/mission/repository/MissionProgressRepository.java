package com.bside.pjt.zerobackend.mission.repository;

import com.bside.pjt.zerobackend.mission.domain.MissionProgress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, Long> {

    Optional<MissionProgress> findFirstByUserIdOrderByIdDesc(final long userId);

    List<MissionProgress> findAllByUserIdOrderByOrder(final long userId);

    List<MissionProgress> findAllByUserIdAndCompletedOrderByOrder(final long userId, final boolean completed);

    int countByUserIdAndCompletedTrue(final long userId);

    int countByUserId(final long userId);
}
