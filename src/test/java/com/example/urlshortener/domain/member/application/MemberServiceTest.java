package com.example.urlshortener.domain.member.application;

import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import com.example.urlshortener.domain.member.dto.MemberResponse;
import com.example.urlshortener.domain.member.dto.SignUpReq;
import com.example.urlshortener.domain.member.dto.SignUpReqBuilder;
import com.example.urlshortener.domain.member.exception.EmailDuplicateException;
import com.example.urlshortener.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberServiceTest extends IntegrationTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;
    private Member member;

    @BeforeEach
    public void setUp() throws Exception {
        member = MemberBuilder.build();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원가입 Form Data를 가지고, 회원 등록한다.")
    public void signup() {
        // given
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignUpReq signUpReq = SignUpReqBuilder.build(email, password);

        // when
        final MemberResponse memberResponse = memberService.registerMember(signUpReq);


        // then
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("이미 가입되어 있는 email로 회원가입 요청이 들어오면, 예외를 던진다.")
    public void signupWithDuplicatedEmail() {
        // given
        memberRepository.save(member);
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignUpReq signUpReq = SignUpReqBuilder.build(email, password);

        // when
        assertThatThrownBy(() -> memberService.registerMember(signUpReq)).isInstanceOf(EmailDuplicateException.class);
    }
}