package com.bside.pjt.zerobackend.mission.domain;

import com.bside.pjt.zerobackend.common.domain.BaseEntity;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionProgress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: User로 변경
    private Long userId;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @OneToMany(mappedBy = "missionProgress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProofImage> proofImages = new ArrayList<>();

    private String evaluation;

    private boolean completed;

    private LocalDateTime completedAt;

    private boolean deleted;

    public MissionProgress(final Long userId, final Mission mission) {
        this.userId = userId;
        this.mission = mission;
    }

    public String missionTitle() {
        return this.mission.getTitle();
    }

    public String missionDescription() {
        return this.mission.getDescription();
    }

    public int missionOrder() {
        return this.mission.getOrder();
    }

    public boolean isCreatedToday() {
        final LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        final LocalDateTime createdAt = this.getCreatedAt().truncatedTo(ChronoUnit.DAYS);

        return today.equals(createdAt);
    }
}
