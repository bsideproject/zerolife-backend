package com.bside.pjt.zerobackend.mission.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class MissionResponse {

    private final String title;
    private final String description;
}
