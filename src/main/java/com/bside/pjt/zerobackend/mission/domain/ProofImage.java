package com.bside.pjt.zerobackend.mission.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProofImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mission_progress_id")
    private MissionProgress missionProgress;

    private String url;

    public ProofImage(final String url) {
        this.url = url;
    }

    public void setMissionProgress(MissionProgress missionProgress) {
        this.missionProgress = missionProgress;
    }
}
