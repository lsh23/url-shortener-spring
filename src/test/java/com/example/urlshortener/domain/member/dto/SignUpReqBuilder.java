package com.example.urlshortener.domain.member.dto;

public class SignUpReqBuilder {

    public static SignUpReq build(String email, String password){
        return createSignUpReq(email, password);
    }

    private static SignUpReq createSignUpReq(String email, String password) {
        return SignUpReq.builder()
                .email(email)
                .password(password)
                .build();
    }
}
