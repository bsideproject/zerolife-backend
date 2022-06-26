package com.bside.pjt.zerobackend.mission.domain;

import com.bside.pjt.zerobackend.common.domain.BaseEntity;
import com.bside.pjt.zerobackend.user.domain.User;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MissionProgress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @OneToMany(mappedBy = "missionProgress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProofImage> proofImages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Evaluation evaluation;

    @Column(name = "\"order\"")
    private Integer order;

    private boolean completed;

    private LocalDateTime completedAt;

    private boolean deleted;

    public MissionProgress(final User user, final Mission mission, final int order) {
        this.user = user;
        this.mission = mission;
        this.order = order;
    }

    public Long userId() {
        return this.user.getId();
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

    public String proofImageUrl() {
        if (proofImages.size() == 0) {
            return null;
        }

        return this.proofImages.get(0).getUrl();
    }

    public boolean isCreatedToday() {
        final LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        final LocalDateTime createdAt = this.getCreatedAt().truncatedTo(ChronoUnit.DAYS);

        return today.equals(createdAt);
    }

    public boolean isAvailableCompleted() {
        final LocalDateTime current = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        final LocalDateTime createdAt = this.getCreatedAt().truncatedTo(ChronoUnit.DAYS);

        return current.equals(createdAt);
    }

    public void completeMission(final ProofImage image, final Evaluation evaluation) {
        this.proofImages.add(image);
        image.setMissionProgress(this);

        this.evaluation = evaluation;
        this.completed = true;
        this.completedAt = LocalDateTime.now();
    }
}
