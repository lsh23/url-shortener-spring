package com.example.urlshortener.domain.member.application;

import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.dto.MemberResponse;
import com.example.urlshortener.domain.member.dto.SignUpReq;
import com.example.urlshortener.domain.member.exception.EmailDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse registerMember(SignUpReq signUpReq) {

        if (memberRepository.existsByEmail(signUpReq.getEmail())) {
            throw new EmailDuplicateException();
        }

        Member newMember = memberRepository.save(signUpReq.toEntity());
        return MemberResponse.of(newMember);
    }
}
