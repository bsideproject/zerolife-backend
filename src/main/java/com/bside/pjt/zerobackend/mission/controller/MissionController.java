package com.bside.pjt.zerobackend.mission.controller;

import com.bside.pjt.zerobackend.mission.controller.response.CurrentMissionProgressResponse;
import com.bside.pjt.zerobackend.mission.service.MissionService;
import com.bside.pjt.zerobackend.mission.service.dto.MissionProgressDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
