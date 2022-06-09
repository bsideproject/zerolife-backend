package com.bside.pjt.zerobackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // E0xxx: 일반/공통 에러

    // E1xxx: 유저 관련 에러

    // E2xxx: 미션 관련 에러
    E2000("존재하지 않는 미션입니다."),

    // E3xxx: 미션 진행 상황 관련 에러
    E3000("오늘의 미션을 받지 않았습니다."),
    E3001("이미 오늘의 미션을 받았습니다.");


    private final String message;
}
