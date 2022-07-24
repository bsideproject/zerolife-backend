package com.bside.pjt.zerobackend.user.domain;

import com.bside.pjt.zerobackend.common.domain.BaseEntity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Table(name = "user", schema = "zerolife")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private boolean requiredAgreement;

    private LocalDateTime requiredAgreedAt;

    private boolean marketingAgreement;

    private LocalDateTime marketingAgreedAt;

    private boolean pushAlarmAgreement;

    private LocalDateTime pushAlarmAgreedAt;

    private String type;

    private boolean deleted;

    public User(final String email, final String nickname, final String password, final boolean marketingAgreement) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.requiredAgreement = true;
        this.requiredAgreedAt = LocalDateTime.now();
        this.marketingAgreement = marketingAgreement;
        if (marketingAgreement) {
            this.marketingAgreedAt = LocalDateTime.now();
        }
        this.type = "USER";
        this.deleted = false;
    }
}
