package com.bside.pjt.zerobackend.mission.repository;

import com.bside.pjt.zerobackend.mission.domain.Mission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Mission> getByOrder(final int order);
}
