package com.bside.pjt.zerobackend.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
    // E0xxx: 일반/공통 에러
    E0000("잘못된 요청입니다."),

    // E1xxx: 유저 관련 에러

    // E2xxx: 미션 관련 에러
    E2000("존재하지 않는 미션입니다."),

    // E3xxx: 미션 진행 상황 관련 에러
    E3000("오늘의 미션을 받지 않았습니다."),
    E3001("이미 오늘의 미션을 받았습니다."),
    E3002("존재하지 않는 데일리 미션입니다."),
    E3003("데일리 미션을 수정할 권한이 없습니다."),
    E3004("이미 인증 완료된 데일리 미션입니다."),
    E3005("인증 기간이 지난 데일리 미션입니다."),
    E3006("데일리 미션 아이디가 누락되었습니다."),
    E3007("데일리 미션 인증 이미지 URL이 누락되었습니다."),
    E3008("데일리 미션 평가 데이터가 누락되었습니다."),
    E3009("잘못된 데일리 미션 평가 데이터입니다.");


    private final String message;
}
