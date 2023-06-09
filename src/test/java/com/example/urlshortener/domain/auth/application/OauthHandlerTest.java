package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.dto.OauthUserInfoResponse;
import com.example.urlshortener.domain.auth.dto.OauthUserInformation;
import com.example.urlshortener.test.MockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class OauthHandlerTest extends MockTest {
    @InjectMocks
    private OauthHandler oauthHandler;
    @Mock
    private OauthRequester oauthRequester;

    @Test
    @DisplayName("provider와, authorization code로 OAuth 인증을 통해서 email을 가져온다.")
    public void getUserInfoFromAuthCode(){
        // given
        OauthUserInfoResponse expected = OauthUserInfoResponse.builder().email("solver@test.com").build();
        final String provider = "github";
        final String code = "code";
        given(oauthRequester.getAccessToken(code)).willReturn("code");
        given(oauthRequester.getUserInfo(any())).willReturn(expected);

        // when
        OauthUserInformation userInfoFromAuthCode = oauthHandler.getUserInfoFromAuthCode(provider, code);

        // then
        assertThat(userInfoFromAuthCode.getEmail()).isEqualTo("solver@test.com");
    }

}