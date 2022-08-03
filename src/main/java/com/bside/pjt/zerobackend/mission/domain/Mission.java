package com.bside.pjt.zerobackend.mission.domain;

import com.bside.pjt.zerobackend.common.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MissionCategory category;

    private String title;

    private String description;

    private String method;

    private String guideImageUrl;

    @Enumerated(EnumType.STRING)
    private MissionLevel level;

    private Integer order;
}
