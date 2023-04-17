package com.example.urlshortener.domain.member.application;

import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import com.example.urlshortener.domain.member.dto.MemberResponse;
import com.example.urlshortener.domain.member.dto.SignUpReq;
import com.example.urlshortener.domain.member.dto.SignUpReqBuilder;
import com.example.urlshortener.domain.member.exception.EmailDuplicateException;
import com.example.urlshortener.test.MockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class MemberServiceTest extends MockTest {
    @InjectMocks
    private MemberService memberService;

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
    public void 회원가입_성공() {
        // given
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignUpReq signUpReq = SignUpReqBuilder.build(email, password);

        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.save(any())).willReturn(member);

        // when
        final MemberResponse memberResponse = memberService.registerMember(signUpReq);


        // then
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    public void 회원가입_실패() {
        // given
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignUpReq signUpReq = SignUpReqBuilder.build(email, password);

        given(memberRepository.existsByEmail(any())).willReturn(true);

        // when
        assertThatThrownBy(() -> memberService.registerMember(signUpReq)).isInstanceOf(EmailDuplicateException.class);
    }

//    @Test
//    public void 멤버리스트_조회() {
//        // given
//        PageRequest pageRequest = PageRequest.of(0, 20);
//        given(memberRepository.findAll(pageRequest)).willReturn(new PageImpl<Member>(Arrays.asList(member)));
//
//        // when
//        final Page<Member> all = memberService.findAll(pageRequest);
//
//        // then
//        assertThat(all.getSize()).isEqualTo(1);
//
//    }
//
//
//    @Test
//    public void 멤버단일_조회() {
//        // given
//        given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.of(member));
//
//        // when
//        MemberResponse memberResponse = memberService.findByEmail(member.getEmail());
//
//        // then
//        assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
//    }


}