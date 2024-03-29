package com.bside.pjt.zerobackend.mission.service;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.exception.ServiceException;
import com.bside.pjt.zerobackend.mission.controller.request.ProveDailyMissionRequest;
import com.bside.pjt.zerobackend.mission.domain.Evaluation;
import com.bside.pjt.zerobackend.mission.domain.Mission;
import com.bside.pjt.zerobackend.mission.domain.MissionProgress;
import com.bside.pjt.zerobackend.mission.domain.ProofImage;
import com.bside.pjt.zerobackend.mission.repository.MissionProgressRepository;
import com.bside.pjt.zerobackend.mission.repository.MissionQueryRepository;
import com.bside.pjt.zerobackend.mission.repository.MissionRepository;
import com.bside.pjt.zerobackend.mission.service.dto.CompletedDailyMissionDto;
import com.bside.pjt.zerobackend.mission.service.dto.DailyMissionProgressDto;
import com.bside.pjt.zerobackend.mission.service.dto.MissionProgressDto;
import com.bside.pjt.zerobackend.reward.event.CreateAchieveRewardEvent;
import com.bside.pjt.zerobackend.user.domain.User;
import com.bside.pjt.zerobackend.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final MissionQueryRepository missionQueryRepository;
    private final MissionProgressRepository missionProgressRepository;
    private final UserRepository userRepository;

    // TODO: 미션 데이터 전달 받으면 60으로 변경
    private final int LAST_MISSION_ORDER = 5;
    private final int NUMBER_OF_MISSION_PER_PAGE = 6;

    @Transactional(readOnly = true)
    public DailyMissionProgressDto findDailyMissionProgress(final long userId) {
        // 1. 현재 탈퇴하지 않은 사용자인지 확인
        User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1000));

        // 2. 현재 사용자가 진행한 데일리 미션 중 가장 최근 것을 조회
        MissionProgress current = missionProgressRepository.findFirstByUserIdOrderByIdDesc(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3000));

        // 3. 조회한 데일리 미션이 없거나 오늘 날짜가 아니라면 에러 발생
        if (!current.isCreatedToday()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3000);
        }

        // 4. 조회한 데일리 미션 + 미션 정보를 반환
        return DailyMissionProgressDto.from(current);
    }

    @Transactional
    public void createMissionProgress(final long userId) {
        // 1. 현재 탈퇴하지 않은 사용자인지 확인
        User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1000));

        // 2. 현재 사용자가 진행한 데일리 미션 중 가장 최근 것을 조회
        final Optional<MissionProgress> current = missionProgressRepository.findFirstByUserIdOrderByIdDesc(userId);

        // 3. 다음 미션 순서 설정
        int currentMissionOrder = 0;
        int currentProgressOrder = 0;
        if (current.isPresent()) {
            // 관리자 계정은 테스트를 위해 제한없이 생성할 수 있도록 변경
            if (user.isNotAdmin() && current.get().isCreatedToday()) {
                throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3001);
            }

            currentMissionOrder = current.get().missionOrder();
            currentProgressOrder = current.get().getOrder();
            if (currentMissionOrder == LAST_MISSION_ORDER) {
                throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3010);
            }
        }

        // 4. 현재까지 진행한 데일리 미션의 다음 미션을 오늘의 미션으로 생성
        // 추후 마지막 미션 달성 후 다시 첫 번째 미션을 받을 수도 있기에 로직은 그대로 유지
        final int rest = (currentMissionOrder + 1) % LAST_MISSION_ORDER;
        final int nextOrder = (rest == 0) ? LAST_MISSION_ORDER : rest;
        final Mission nextMission = missionRepository.findByOrder(nextOrder)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E2000));
        final MissionProgress today = new MissionProgress(user, nextMission, currentProgressOrder + 1);

        missionProgressRepository.save(today);
    }

    @Transactional
    public CreateAchieveRewardEvent updateMissionProgress(final long userId, final long missionProgressId, final ProveDailyMissionRequest request) {
        // 1. 현재 탈퇴하지 않은 사용자인지 확인
        User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1000));

        // 2. 데일리 미션 조회
        final MissionProgress missionProgress = missionProgressRepository.findById(missionProgressId)
            .filter(m -> !m.isDeleted())
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3002));

        // 3. 주어진 데일리 미션을 업데이트 할 수 있는 사용자인지 확인
        if (missionProgress.userId() != userId) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3003);
        }

        // 4. 인증 완료됐는지 확인
        if (missionProgress.isCompleted()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3004);
        }

        // 5. 이미 하루가 지났는지 확인
        if (!missionProgress.isAvailableCompleted()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E3005);
        }

        // 6. 데일리 미션 인증 정보 업데이트
        final ProofImage proofImage = new ProofImage(request.getProofImageUrl().getBytes(StandardCharsets.UTF_8));
        missionProgress.completeMission(proofImage, Evaluation.valueOf(request.getEvaluation()));

        return new CreateAchieveRewardEvent(userId, missionProgress.getOrder());
    }

    @Transactional(readOnly = true)
    public List<MissionProgressDto> findMissionProgressList(final long userId) {
        // 1. 현재 탈퇴하지 않은 사용자인지 확인
        User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1000));

        // 2. 현재 사용자의 모든 데일리 미션 조회
        final List<MissionProgress> missionProgressList = missionProgressRepository.findAllByUserIdOrderByOrder(userId);

        // 3. 2번 결과로 DTO 생성
        final List<MissionProgressDto> result;
        result = missionProgressList.stream()
            .map(missionProgress -> MissionProgressDto.builder()
                .missionProgressId(missionProgress.getId())
                .missionTitle(missionProgress.missionTitle())
                .progressOrder(missionProgress.getOrder())
                .isCompleted(missionProgress.isCompleted())
                .build())
            .collect(Collectors.toList());

        // 4. 화면에 추가적으로 노출해야 하는 미션 수 계산
        final int needed = calculateNumberOfNeededMission(missionProgressList.size());

        // 5. 4번에서 계산한 수만큼 미션 조회 & DTO 생성
        final int size = missionProgressList.size();
        final AtomicInteger index = new AtomicInteger(size + 1);
        if (needed != 0) {
            final int lastMissionOrder = missionProgressList.get(size - 1).missionOrder();
            final List<Mission> neededMissions = missionQueryRepository.findByIdStartsWith(lastMissionOrder + 1, needed);
            neededMissions.stream()
                .map(mission -> new MissionProgressDto(mission.getTitle(), index.getAndIncrement()))
                .forEach(result::add);
        }

        // 6. (데일리 미션 + 미션 정보) 목록 반환
        return result;
    }

    private int calculateNumberOfNeededMission(final int totalCountOfMissionProgress) {
        if (totalCountOfMissionProgress == 0) {
            return NUMBER_OF_MISSION_PER_PAGE;
        }

        final int rest = totalCountOfMissionProgress % NUMBER_OF_MISSION_PER_PAGE;

        return rest == 0 ? rest : NUMBER_OF_MISSION_PER_PAGE - rest;
    }

    @Transactional(readOnly = true)
    public List<CompletedDailyMissionDto> findCompletedMissionProgressList(final long userId) {
        // 1. 현재 탈퇴하지 않은 사용자인지 확인
        User user = userRepository.findByIdAndDeletedFalse(userId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1000));

        // 2. 인증 완료된 데일리 미션 목록 조회
        return missionProgressRepository.findAllByUserIdAndCompletedOrderByOrder(userId, true).stream()
            .map(missionProgress -> CompletedDailyMissionDto.builder()
                .missionProgressId(missionProgress.getId())
                .missionTitle(missionProgress.missionTitle())
                .progressOrder(missionProgress.getOrder())
                .proofImageUrl(new String(missionProgress.proofImageUrl(), StandardCharsets.UTF_8))
                .evaluation(missionProgress.getEvaluation())
                .completedAt(missionProgress.getCompletedAt())
                .build())
            .collect(Collectors.toList());
    }
}
