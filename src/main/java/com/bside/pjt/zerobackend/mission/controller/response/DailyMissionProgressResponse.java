package com.bside.pjt.zerobackend.mission.controller.response;

import com.bside.pjt.zerobackend.mission.service.dto.DailyMissionProgressDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class DailyMissionProgressResponse {

    private final MissionResponse mission;
    private final MissionProgressResponse missionProgress;
    private final Integer daysOfProgress;

    public static DailyMissionProgressResponse from(final DailyMissionProgressDto dto) {
        return new DailyMissionProgressResponse(
            new MissionResponse(dto.getMissionTitle(), dto.getMissionDescription()),
            new MissionProgressResponse(dto.getMissionProgressId(), dto.getProofImageUrl(), dto.getIsCompleted(), dto.getCreatedAt()),
            dto.getDaysOfProgress()
        );
    }
}
