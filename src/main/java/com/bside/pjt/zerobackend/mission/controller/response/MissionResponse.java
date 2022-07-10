package com.bside.pjt.zerobackend.mission.controller.response;

import com.bside.pjt.zerobackend.mission.domain.MissionCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class MissionResponse {

    private final MissionCategory category;
    private final String title;
    private final String description;
}
