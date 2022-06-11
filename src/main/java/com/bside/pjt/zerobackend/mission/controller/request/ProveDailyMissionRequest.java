package com.bside.pjt.zerobackend.mission.controller.request;

import com.bside.pjt.zerobackend.common.validation.ValidEvaluation;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class ProveDailyMissionRequest {

    @NotNull(message = "E3006")
    final Long missionProgressId;

    @NotNull(message = "E3007")
    final String proofImageUrl;

    @ValidEvaluation
    final String evaluation;
}
