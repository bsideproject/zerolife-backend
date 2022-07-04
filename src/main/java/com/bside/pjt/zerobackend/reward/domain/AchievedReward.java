package com.bside.pjt.zerobackend.reward.domain;

import com.bside.pjt.zerobackend.common.domain.BaseEntity;
import com.bside.pjt.zerobackend.user.domain.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AchievedReward extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "reward_id")
    private Reward reward;

    private Integer progressOrder;

    private Boolean checked;

    public AchievedReward(final User user, final Reward reward, final int progressOrder) {
        this.user = user;
        this.reward = reward;
        this.progressOrder = progressOrder;
        this.checked = false;
    }

    public String rewardName() {
        return this.reward.getName();
    }

    public int rewardRequirement() {
        return this.reward.getRequirement();
    }

    public String rewardImageUrl() {
        return this.reward.getImageUrl();
    }

    public int rewardOrder() {
        return this.reward.getOrder();
    }

    public void checked() {
        this.checked = true;
    }
}
