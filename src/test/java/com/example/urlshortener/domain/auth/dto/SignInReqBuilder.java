package com.example.urlshortener.domain.auth.dto;

public class SignInReqBuilder {

    public static SignInReq build(String email, String password){
        return createSignUpReq(email, password);
    }

    private static SignInReq createSignUpReq(String email, String password){
        return SignInReq.builder()
                .email(email)
                .password(password)
                .build();
    }
}
