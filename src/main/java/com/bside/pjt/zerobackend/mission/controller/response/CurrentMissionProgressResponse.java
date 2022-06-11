package com.bside.pjt.zerobackend.mission.controller.response;

import com.bside.pjt.zerobackend.mission.service.dto.MissionProgressDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class CurrentMissionProgressResponse {

    private final MissionResponse mission;
    private final MissionProgressResponse missionProgress;
    private final Long daysOfProgress;

    public static CurrentMissionProgressResponse from(final MissionProgressDto dto) {
        return new CurrentMissionProgressResponse(
            new MissionResponse(dto.getMissionTitle(), dto.getMissionDescription()),
            new MissionProgressResponse(dto.getMissionProgressId(), dto.getProofImageUrl(), dto.getIsCompleted(), dto.getCreatedAt()),
            dto.getDaysOfProgress()
        );
    }
}
