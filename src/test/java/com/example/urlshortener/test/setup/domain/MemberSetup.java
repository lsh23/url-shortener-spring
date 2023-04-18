package com.example.urlshortener.test.setup.domain;

import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


@Component
@RequiredArgsConstructor
public class MemberSetup {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final String rawPassword = "solver";
    public Member save() {
        final Member member = MemberBuilder.build(
                "solver@test.com",
                passwordEncoder.encode(rawPassword)
        );
        return memberRepository.save(member);
    }

    public String getRawPassword() {
        return rawPassword;
    }
    public List<Member> save(int count) {
        List<Member> members = new ArrayList<>();

        IntStream.range(0, count)
                .forEach((i) -> members.add(MemberBuilder.build(
                        "solver" + i + "@test.com",
                        passwordEncoder.encode(rawPassword)
                )));
        return memberRepository.saveAll(members);
    }
}
