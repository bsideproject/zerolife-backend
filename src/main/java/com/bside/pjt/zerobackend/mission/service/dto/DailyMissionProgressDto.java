package com.bside.pjt.zerobackend.mission.service.dto;

import com.bside.pjt.zerobackend.mission.domain.MissionCategory;
import com.bside.pjt.zerobackend.mission.domain.MissionProgress;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public final class DailyMissionProgressDto {

    private final MissionCategory missionCategory;
    private final String missionTitle;
    private final String missionDescription;

    private final Long missionProgressId;
    private final String proofImageUrl;
    private final Boolean isCompleted;
    private final LocalDateTime createdAt;

    private final Integer daysOfProgress;

    public static DailyMissionProgressDto from(final MissionProgress missionProgress) {
        String proofImageUrl = null;
        if (missionProgress.getProofImages().size() != 0) {
            proofImageUrl = missionProgress.getProofImages().get(0).getUrl();
        }

        return DailyMissionProgressDto.builder()
            .missionCategory(missionProgress.missionCategory())
            .missionTitle(missionProgress.missionTitle())
            .missionDescription(missionProgress.missionDescription())
            .missionProgressId(missionProgress.getId())
            .proofImageUrl(proofImageUrl)
            .isCompleted(missionProgress.isCompleted())
            .createdAt(missionProgress.getCreatedAt())
            .daysOfProgress(missionProgress.getOrder())
            .build();
    }
}
