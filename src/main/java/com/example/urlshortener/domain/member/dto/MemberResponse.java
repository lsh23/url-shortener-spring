package com.example.urlshortener.domain.member.dto;

import com.example.urlshortener.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private Long id;
    private String email;

    @Builder
    private MemberResponse(Long id, String email){
        this.id = id;
        this.email = email;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail());
    }
}



