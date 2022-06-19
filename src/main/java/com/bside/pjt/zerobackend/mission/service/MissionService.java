package com.bside.pjt.zerobackend.mission.service;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.ServiceException;
import com.bside.pjt.zerobackend.mission.controller.request.ProveDailyMissionRequest;
import com.bside.pjt.zerobackend.mission.domain.Evaluation;
import com.bside.pjt.zerobackend.mission.domain.Mission;
import com.bside.pjt.zerobackend.mission.domain.MissionProgress;
import com.bside.pjt.zerobackend.mission.domain.ProofImage;
import com.bside.pjt.zerobackend.mission.repository.MissionProgressRepository;
import com.bside.pjt.zerobackend.mission.repository.MissionRepository;
import com.bside.pjt.zerobackend.mission.service.dto.MissionProgressDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {

    public final MissionRepository missionRepository;
    public final MissionProgressRepository missionProgressRepository;

    @Transactional(readOnly = true)
    public MissionProgressDto findDailyMissionProgress(final long userId) {
        // TODO: 1. 현재 탈퇴하지 않은 사용자인지 확인

        // 2. 현재 사용자가 진행한 데일리 미션 중 가장 최근 것을 조회
        MissionProgress current = missionProgressRepository.getFirstByUserIdOrderByIdDesc(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3000));

        // 3. 조회한 데일리 미션이 없거나 오늘 날짜가 아니라면 에러 발생
        if (!current.isCreatedToday()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3000);
        }

        // 4. 현재 사용자가 총 며칠 째 미션을 진행 중인지 계산
        final long daysOfProgress = missionProgressRepository.countByUserId(userId);

        // 5. 조회한 데일리 미션 + 미션 정보를 반환
        return MissionProgressDto.of(current, daysOfProgress);
    }

    @Transactional
    public void createDailyMissionProgress(final long userId) {
        // TODO: 1. 현재 탈퇴하지 않은 사용자인지 확인

        // 2. 현재 사용자가 진행한 데일리 미션 중 가장 최근 것을 조회
        final Optional<MissionProgress> current = missionProgressRepository.getFirstByUserIdOrderByIdDesc(userId);

        // 3. 다음 미션 순서 설정
        int currentMissionOrder = 0;
        if (current.isPresent()) {
            if (current.get().isCreatedToday()) {
                throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3001);
            }
            currentMissionOrder = current.get().missionOrder();
        }

        // 4. 현재까지 진행한 데일리 미션의 다음 미션을 오늘의 미션으로 생성
        final Mission nextMission = missionRepository.getByOrder(currentMissionOrder + 1)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E2000));
        final MissionProgress today = new MissionProgress(userId, nextMission);

        missionProgressRepository.save(today);
    }

    @Transactional
    public void updateDailyMissionProgress(final long userId, final long missionProgressId, final ProveDailyMissionRequest request) {
        // 1. 데일리 미션 조회
        final MissionProgress missionProgress = missionProgressRepository.findById(missionProgressId)
            .filter(m -> !m.isDeleted())
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3002));

        // TODO: 2. 주어진 데일리 미션을 업데이트 할 수 있는 사용자인지 확인
        if (missionProgress.getUserId() != userId) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3003);
        }

        // 3. 인증 완료됐는지 확인
        if (missionProgress.isCompleted()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3004);
        }

        // 4. 이미 하루가 지났는지 확인
        if (!missionProgress.isAvailableCompleted()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3005);
        }

        // 5. 데일리 미션 인증 정보 업데이트
        final ProofImage proofImage = new ProofImage(request.getProofImageUrl());
        missionProgress.completeMission(proofImage, Evaluation.valueOf(request.getEvaluation()));
    }
}
