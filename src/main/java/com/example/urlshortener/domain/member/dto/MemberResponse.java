package com.example.urlshortener.domain.member.dto;

import com.example.urlshortener.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private String email;

    private MemberResponse(String email){
        this.email = email;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getEmail());
    }
}



