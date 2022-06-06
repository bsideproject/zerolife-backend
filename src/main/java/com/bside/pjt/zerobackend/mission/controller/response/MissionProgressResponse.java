package com.bside.pjt.zerobackend.mission.controller.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MissionProgressResponse {

    private final Long id;
    private final String proofImageUrl;
    private final Boolean isCompleted;
    private final LocalDateTime createdAt;
}
