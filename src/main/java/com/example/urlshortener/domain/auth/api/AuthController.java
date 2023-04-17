package com.example.urlshortener.domain.auth.api;

import com.example.urlshortener.domain.auth.application.AuthService;
import com.example.urlshortener.domain.auth.dto.SignInReq;
import com.example.urlshortener.domain.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class AuthController {
    final private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<MemberResponse> signIn(@RequestBody final SignInReq signInReq) {
        return ResponseEntity.ok(authService.signIn(signInReq));
    }
}
