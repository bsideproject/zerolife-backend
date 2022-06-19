package com.bside.pjt.zerobackend.mission.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public final class MissionProgressDto {

    private final Long missionProgressId;
    private final String missionTitle;
    private final Integer progressOrder;
    private final Boolean isCompleted;

    public MissionProgressDto(final String missionTitle, final int progressOrder) {
        this.missionProgressId = 0L;
        this.missionTitle = missionTitle;
        this.progressOrder = progressOrder;
        this.isCompleted = false;
    }
}
