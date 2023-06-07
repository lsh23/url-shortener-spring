package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.dto.OauthUserInfoResponse;
import com.example.urlshortener.domain.auth.dto.OauthUserInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OauthHandler {

    private final OauthRequester oauthRequester;
    public OauthUserInformation getUserInfoFromAuthCode(String provider, String code) {
        // access code 얻기
        String accessToken = oauthRequester.getAccessToken(code);
        // user info 얻기
        OauthUserInfoResponse userInfoResponse = oauthRequester.getUserInfo(accessToken);
        return OauthUserInformation.of(userInfoResponse);
    }




}
