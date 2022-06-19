package com.bside.pjt.zerobackend.mission.repository;

import com.bside.pjt.zerobackend.mission.domain.MissionProgress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, Long> {

    Optional<MissionProgress> findFirstByUserIdOrderByIdDesc(final long userId);

    long countByUserId(final long userId);

    List<MissionProgress> findAllByUserId(final long userId);
}
