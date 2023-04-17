package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.dto.SignInReq;
import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.dto.MemberResponse;
import com.example.urlshortener.domain.member.exception.EmailNotFoundException;
import com.example.urlshortener.domain.member.exception.PasswordInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponse signIn(SignInReq signInReq) {

        if (memberRepository.existsByEmail(signInReq.getEmail()) == false) {
            throw new EmailNotFoundException();
        }

        Member member = memberRepository.findByEmail(signInReq.getEmail()).get();

        if (!checkPasswordMatch(signInReq.getPassword(), member)) {
            throw new PasswordInvalidException();
        }

        return MemberResponse.of(member);
    }

    private boolean checkPasswordMatch(String reqPassword, Member member) {
        return passwordEncoder.matches(reqPassword, member.getPassword());
    }
}
