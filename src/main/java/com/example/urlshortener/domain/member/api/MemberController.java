package com.example.urlshortener.domain.member.api;

import com.example.urlshortener.domain.member.application.MemberService;
import com.example.urlshortener.domain.member.dto.MemberResponse;
import com.example.urlshortener.domain.member.dto.SignUpReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> registerMember(@RequestBody final SignUpReq signUpReq){
        signUpReq.setPassword(passwordEncoder.encode(signUpReq.getPassword()));
        return ResponseEntity.ok(memberService.registerMember(signUpReq));
    }
}
