package com.bside.pjt.zerobackend.mission.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

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

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] url;

    private boolean deleted;

    public ProofImage(final byte[] url) {
        this.url = url;
    }

    public void setMissionProgress(MissionProgress missionProgress) {
        this.missionProgress = missionProgress;
    }

    public void delete() {
        this.deleted = true;
    }
}
