package com.bside.pjt.zerobackend.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    // TODO: 테스트 용 임시 URL
    @GetMapping
    public ResponseEntity<String> main() {
        return ResponseEntity.ok("🙌🏻Hello Zerolife🙌🏻");
    }

    @GetMapping("apis/oauth/kakao")
    public ResponseEntity<String> socialLoginForKakao() {
        return ResponseEntity.ok("{code: 200, message: 카카오 소셜 로그인 성공}");
    }
}
