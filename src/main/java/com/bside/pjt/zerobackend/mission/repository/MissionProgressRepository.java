package com.bside.pjt.zerobackend.mission.repository;

import com.bside.pjt.zerobackend.mission.domain.MissionProgress;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, Long> {

    Optional<MissionProgress> getFirstByUserIdOrderByIdDesc(final long userId);

    long countByUserId(final long userId);
}
