package com.bside.pjt.zerobackend.mission.controller;

import com.bside.pjt.zerobackend.mission.controller.request.ProveDailyMissionRequest;
import com.bside.pjt.zerobackend.mission.controller.response.DailyMissionProgressResponse;
import com.bside.pjt.zerobackend.mission.service.MissionService;
import com.bside.pjt.zerobackend.mission.service.dto.DailyMissionProgressDto;
import com.bside.pjt.zerobackend.mission.service.dto.MissionProgressDto;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    private final MissionService missionService;

    @GetMapping("apis/daily-mission-progress")
    public ResponseEntity<DailyMissionProgressResponse> findDailyMission() {

        // TODO: 추후 토큰에서 가져오는 로직 추가
        final Long userId = 1L;
        final DailyMissionProgressDto result = missionService.findDailyMissionProgress(userId);

        return ResponseEntity.ok(DailyMissionProgressResponse.from(result));
    }

    @PostMapping("apis/mission-progress")
    public ResponseEntity<Void> createDailyMission() {

        // TODO: 추후 토큰에서 가져오는 로직 추가
        final Long userId = 1L;
        missionService.createMissionProgress(userId);

        return ResponseEntity.created(URI.create("apis/daily-mission-progress")).build();
    }

    @PutMapping("apis/mission-progress/{missionProgressId}")
    public ResponseEntity<Void> proveDailyMission(
        @PathVariable final Long missionProgressId,
        @RequestBody @Valid final ProveDailyMissionRequest request
    ) {

        // TODO: 추후 토큰에서 가져오는 로직 추가
        final Long userId = 1L;
        missionService.updateMissionProgress(userId, missionProgressId, request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("apis/mission-progress")
    public ResponseEntity<List<MissionProgressDto>> findMissionProgressList() {

        // TODO: 추후 토큰에서 가져오는 로직 추가
        final Long userId = 1L;
        final List<MissionProgressDto> result = missionService.findMissionProgressList(userId);

        return ResponseEntity.ok(result);
    }
}
