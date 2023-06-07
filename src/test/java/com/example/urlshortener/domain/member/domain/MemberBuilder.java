package com.example.urlshortener.domain.member.domain;

public class MemberBuilder {
    public static Member build(){
        final String email = "solver@test.com";
        final String password = "password";
        return createMember(email, password);
    }
    public static Member build(String email, String password) {
        return createMember(email, password);
    }
    private static Member createMember(String email, String password) {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }

}
