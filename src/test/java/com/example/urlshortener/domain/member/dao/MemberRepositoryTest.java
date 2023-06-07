package com.example.urlshortener.domain.member.dao;

import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.test.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member saveMember;
    private String email;

    @BeforeEach
    public void setUp() throws Exception {
        final String email = "solver@test.com";
        this.email = email;
        final String password = "solver";
        Member build = Member.builder().email(email).password(password).build();
        saveMember = memberRepository.save(build);
    }

    @Test
    @DisplayName("email로 회원존재 여부을 조회한다.")
    public void existsByEmail() {
        boolean existsByEmail = memberRepository.existsByEmail(email);
        assertThat(existsByEmail).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 email로 회원존재 여부을 조회하면 False를 반환한다.")
    public void existsByEmailWithInvalidEmail() {
        boolean existsByEmail = memberRepository.existsByEmail("tpgud@test.com");
        assertThat(existsByEmail).isFalse();
    }

    @Test
    @DisplayName("email로 회원을 조회한다.")
    public void findByEmail() {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        assertThat(byEmail.get()).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 email로 회원을 조회하면 예외를 던진다.")

    public void findByEmailWithInvalidEmail() {
        Optional<Member> byEmail = memberRepository.findByEmail("tpgud@test.com");
        assertThatThrownBy(() -> byEmail.get()).isInstanceOf(NoSuchElementException.class);
    }
}