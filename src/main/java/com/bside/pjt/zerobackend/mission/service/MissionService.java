package com.bside.pjt.zerobackend.mission.service;

import com.bside.pjt.zerobackend.mission.domain.Mission;
import com.bside.pjt.zerobackend.mission.domain.MissionProgress;
import com.bside.pjt.zerobackend.mission.repository.MissionProgressRepository;
import com.bside.pjt.zerobackend.mission.repository.MissionRepository;
import com.bside.pjt.zerobackend.mission.service.dto.MissionProgressDto;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionService {

    public final MissionRepository missionRepository;
    public final MissionProgressRepository missionProgressRepository;

    @Transactional
    public MissionProgressDto findDailyMissionProgress(final Long userId) {
        // 1. 현재 탈퇴하지 않은 사용자인지 확인

        // 2. 현재 사용자가 진행한 데일리 미션 중 가장 최근 것을 조회
        Optional<MissionProgress> current = missionProgressRepository.getFirstByUserIdOrderByIdDesc(userId);

        // 3. 조회한 데일리 미션이 없거나 오늘 날짜가 아니라면 새로 데일리 미션을 생성
        if (current.isEmpty()) {
            final Mission firstMission = missionRepository.getByOrder(1).orElseThrow();

            final MissionProgress today = new MissionProgress(userId, firstMission);
            missionProgressRepository.save(today);

            current = Optional.of(today);
        }

        if (!current.get().isCreatedToday()) {
            final Mission nextMission = missionRepository.getByOrder(current.get().missionOrder() + 1).orElseThrow();
            final MissionProgress today = new MissionProgress(userId, nextMission);
            missionProgressRepository.save(today);

            current = Optional.of(today);
        }

        // 4. 현재 사용자가 총 며칠 째 미션을 진행 중인지 계산
        final long daysOfProgress = missionProgressRepository.countByUserId(userId);

        // 5. 조회/생성한 데일리 미션 + 미션 정보를 반환
        return MissionProgressDto.of(current.get(), daysOfProgress);
    }
}
