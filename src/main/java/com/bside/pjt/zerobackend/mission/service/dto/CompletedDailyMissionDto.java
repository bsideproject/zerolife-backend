package com.bside.pjt.zerobackend.mission.service.dto;

import com.bside.pjt.zerobackend.mission.domain.Evaluation;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public final class CompletedDailyMissionDto {

    private final Long missionProgressId;
    private final String missionTitle;
    private final Integer progressOrder;
    private final String proofImageUrl;
    private final Evaluation evaluation;
    private final LocalDateTime completedAt;
}
