package com.bside.pjt.zerobackend.mission.controller;

import com.bside.pjt.zerobackend.mission.controller.request.ProveDailyMissionRequest;
import com.bside.pjt.zerobackend.mission.controller.response.CurrentMissionProgressResponse;
import com.bside.pjt.zerobackend.mission.service.MissionService;
import com.bside.pjt.zerobackend.mission.service.dto.MissionProgressDto;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<CurrentMissionProgressResponse> findDailyMissionProgress() {

        // TODO: 추후 토큰에서 가져오는 로직 추가
        final Long userId = 1L;
        final MissionProgressDto result = missionService.findDailyMissionProgress(userId);

        return ResponseEntity.ok(CurrentMissionProgressResponse.from(result));
    }

    @PostMapping("apis/daily-mission-progress")
    public ResponseEntity<Void> createDailyMissionProgress() {

        // TODO: 추후 토큰에서 가져오는 로직 추가
        final Long userId = 1L;
        missionService.createDailyMissionProgress(userId);

        return ResponseEntity.created(URI.create("apis/daily-mission-progress")).build();
    }

    @PutMapping("apis/daily-mission-progress")
    public ResponseEntity<Void> proveDailyMission(@RequestBody @Valid final ProveDailyMissionRequest request) {

        // TODO: 추후 토큰에서 가져오는 로직 추가
        final Long userId = 1L;
        missionService.updateDailyMissionProgress(userId, request);

        return ResponseEntity.noContent().build();
    }
}
