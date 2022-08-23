package com.bside.pjt.zerobackend.mission.controller;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.common.exception.ServiceException;
import com.bside.pjt.zerobackend.common.security.jwt.JwtPrincipal;
import com.bside.pjt.zerobackend.mission.controller.request.ProveDailyMissionRequest;
import com.bside.pjt.zerobackend.mission.controller.response.DailyMissionProgressResponse;
import com.bside.pjt.zerobackend.mission.service.MissionService;
import com.bside.pjt.zerobackend.mission.service.dto.DailyMissionProgressDto;
import com.bside.pjt.zerobackend.mission.service.dto.MissionProgressDto;
import com.bside.pjt.zerobackend.reward.event.CreateAchieveRewardEvent;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MissionController {

    private final ApplicationEventPublisher publisher;

    private final MissionService missionService;

    @GetMapping("apis/daily-mission-progress")
    public ResponseEntity<DailyMissionProgressResponse> findDailyMission(
        @AuthenticationPrincipal final JwtPrincipal principal
    ) {
        final Long userId = principal.getId();
        if (userId == null || userId == 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1002);
        }

        // 리워드 획득 테스트를 위해 테스터 계정의 경우, 데일리 미션을 조회할 때마다 새로운 데일리 미션을 생성하도록 수정
        if (Objects.equals(principal.getType(), "TESTER")) {
            missionService.createMissionProgress(userId);
        }

        final DailyMissionProgressDto result = missionService.findDailyMissionProgress(userId);

        return ResponseEntity.ok(DailyMissionProgressResponse.from(result));
    }

    @PostMapping("apis/mission-progress")
    public ResponseEntity<Void> createDailyMission(
        @AuthenticationPrincipal final JwtPrincipal principal
    ) {
        final Long userId = principal.getId();
        if (userId == null || userId == 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1002);
        }

        missionService.createMissionProgress(userId);

        return ResponseEntity.created(URI.create("apis/daily-mission-progress")).build();
    }

    @PutMapping("apis/mission-progress/{missionProgressId}")
    public ResponseEntity<Void> proveDailyMission(
        @AuthenticationPrincipal final JwtPrincipal principal,
        @PathVariable final Long missionProgressId,
        @RequestBody @Valid final ProveDailyMissionRequest request
    ) {
        final Long userId = principal.getId();
        if (userId == null || userId == 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1002);
        }

        final CreateAchieveRewardEvent event = missionService.updateMissionProgress(userId, missionProgressId, request);
        publisher.publishEvent(event);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("apis/mission-progress")
    public ResponseEntity<List<MissionProgressDto>> findMissionProgressList(
        @AuthenticationPrincipal final JwtPrincipal principal
    ) {
        final Long userId = principal.getId();
        if (userId == null || userId == 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.E1002);
        }

        final List<MissionProgressDto> result = missionService.findMissionProgressList(userId);

        return ResponseEntity.ok(result);
    }
}
