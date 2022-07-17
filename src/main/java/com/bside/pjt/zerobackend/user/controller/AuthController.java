package com.bside.pjt.zerobackend.user.controller;

import com.bside.pjt.zerobackend.user.controller.request.SignUpRequest;
import com.bside.pjt.zerobackend.user.service.UserService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // TODO: 테스트 용 임시 URL
    @GetMapping
    public ResponseEntity<String> main() {
        return ResponseEntity.ok("Hello Zerolife");
    }

    @PostMapping("/apis/users")
    public ResponseEntity<Void> signUp(@RequestBody @Valid final SignUpRequest request) {
        userService.create(request);

        return ResponseEntity.created(URI.create("/apis/auth/token")).build();
    }
}
