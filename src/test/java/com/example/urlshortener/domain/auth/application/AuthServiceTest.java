package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.dto.SignInReq;
import com.example.urlshortener.domain.auth.dto.SignInReqBuilder;
import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import com.example.urlshortener.domain.member.dto.MemberResponse;
import com.example.urlshortener.domain.member.exception.EmailNotFoundException;
import com.example.urlshortener.domain.member.exception.PasswordInvalidException;
import com.example.urlshortener.test.MockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class AuthServiceTest extends MockTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Member member;

    @BeforeEach
    public void setUp() throws Exception {
        member = MemberBuilder.build();
    }

    @Test
    public void 로그인_성공() {
        // given
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignInReq signInReq = SignInReqBuilder.build(email, password);

        given(memberRepository.existsByEmail(signInReq.getEmail())).willReturn(true);
        given(memberRepository.findByEmail(signInReq.getEmail())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        // when
        MemberResponse memberResponse = authService.signIn(signInReq);

        // then
        assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    public void 로그인_실패_존재하지않는_이메일() {
        // given
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignInReq signInReq = SignInReqBuilder.build(email, password);

        given(memberRepository.existsByEmail(signInReq.getEmail())).willReturn(false);

        int i = 0;
        for (; i < 10; i++) {
            System.out.println("i = " + i);
        }
        System.out.println("i = " + i);
        // when
        assertThatThrownBy(() -> authService.signIn(signInReq)).isInstanceOf(EmailNotFoundException.class);
    }

    @Test
    public void 로그인_실패_패스워드_불일치() {
        // given
        final String email = member.getEmail();
        final String password = "wrong";
        final SignInReq signInReq = SignInReqBuilder.build(email, password);

        given(memberRepository.existsByEmail(signInReq.getEmail())).willReturn(true);
        given(memberRepository.findByEmail(signInReq.getEmail())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(false);

        // when
        assertThatThrownBy(() -> authService.signIn(signInReq)).isInstanceOf(PasswordInvalidException.class);
    }
}