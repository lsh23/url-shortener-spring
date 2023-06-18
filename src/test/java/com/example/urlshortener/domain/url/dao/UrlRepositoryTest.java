package com.example.urlshortener.domain.url.dao;

import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import com.example.urlshortener.domain.url.domain.Url;
import com.example.urlshortener.test.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UrlRepositoryTest extends RepositoryTest {
    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Hash 값으로 URL을 조회한다.")
    void findByHash() {
        // given
        Url url = urlRepository.save(Url.of("www.test.com", "hash"));

        // when
        Url urlByHash= urlRepository.findByHash("hash").get();

        // then
        assertThat(urlByHash.getFullUrl()).isEqualTo("www.test.com");
        assertThat(urlByHash.getHash()).isEqualTo("hash");
    }


    @Test
    @DisplayName("유효하지 않은 Hash 값으로 URL을 조회할 경우 예외를 던진다.")
    void findByHashByInvalidHash() {
        // given
        Url url = urlRepository.save(Url.of("www.test.com", "hash"));

        // when then
        assertThatThrownBy(()->{
            urlRepository.findByHash("invalidHash").get();
        }).isInstanceOf(NoSuchElementException.class);

    }

    @Test
    @DisplayName("MemberId로 URL을 조회한다.")
    void findAllByMemberId() {
        // given
        Member member = MemberBuilder.build();
        memberRepository.save(member);

        Url url = Url.of("www.test.com", "hash");
        url.assignMember(member);
        urlRepository.save(url);

        Url url_2 = Url.of("www.test2.com", "hash2");
        url_2.assignMember(member);
        urlRepository.save(url_2);

        // when
        List<Url> urls = urlRepository.findAllByMemberId(member.getId());

        // then
        assertThat(urls.size()).isEqualTo(2);

    }

}