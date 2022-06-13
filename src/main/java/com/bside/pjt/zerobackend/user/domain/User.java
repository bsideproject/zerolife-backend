package com.bside.pjt.zerobackend.user.domain;

import com.bside.pjt.zerobackend.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long socialMemberNumber;

    private String socialLoginPlatform;

    private String name;

    private String nickname;

    private String email;

    private String profileImageUrl;

    private String profileMessage;

    private boolean requiredAgreement;

    private LocalDateTime requiredAgreedAt;

    private boolean marketingAgreement;

    private LocalDateTime marketingAgreedAt;

    private boolean pushAlarmAgreement;

    private LocalDateTime pushAlarmAgreedAt;

    private String type;

    private boolean deleted;
}
