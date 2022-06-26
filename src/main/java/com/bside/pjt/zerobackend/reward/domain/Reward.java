package com.bside.pjt.zerobackend.reward.domain;

import com.bside.pjt.zerobackend.common.domain.BaseEntity;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reward extends BaseEntity {

    private Long id;

    private String name;

    private Integer requirement;

    private String imageUrl;
}
