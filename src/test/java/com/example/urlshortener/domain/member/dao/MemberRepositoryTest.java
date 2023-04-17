package com.example.urlshortener.domain.member.dao;

import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.test.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
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
    public void existsByEmail_존재하는_경우() {
        boolean existsByEmail = memberRepository.existsByEmail(email);
        assertThat(existsByEmail).isTrue();
    }

    @Test
    public void existsByEmail_존재하지않는_경우() {
        boolean existsByEmail = memberRepository.existsByEmail("tpgud@test.com");
        assertThat(existsByEmail).isFalse();
    }

    @Test
    public void findByEmail_존재하는_경우() {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        assertThat(byEmail.get()).isNotNull();
    }

    @Test
    public void findByEmail_존재하지않는_경우() {
        Optional<Member> byEmail = memberRepository.findByEmail("tpgud@test.com");
        assertThatThrownBy(() -> byEmail.get()).isInstanceOf(NoSuchElementException.class);
    }
}