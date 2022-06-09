package com.bside.pjt.zerobackend.mission.controller.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MissionResponse {

    private final String title;
    private final String description;
}
