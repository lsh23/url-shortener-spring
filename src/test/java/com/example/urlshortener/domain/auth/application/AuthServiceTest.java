package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.dao.RefreshTokenRepository;
import com.example.urlshortener.domain.auth.dao.SessionRepository;
import com.example.urlshortener.domain.auth.dto.*;
import com.example.urlshortener.domain.auth.exception.AlreadySessionExist;
import com.example.urlshortener.domain.auth.exception.DuplicatedSessionUUID;
import com.example.urlshortener.domain.auth.exception.InvalidRefreshToken;
import com.example.urlshortener.domain.auth.exception.TokenNotMatchedByEmail;
import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.domain.MemberBuilder;
import com.example.urlshortener.domain.member.exception.EmailNotFoundException;
import com.example.urlshortener.domain.member.exception.PasswordInvalidException;
import com.example.urlshortener.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class AuthServiceTest extends IntegrationTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Member member;

    @BeforeEach
    public void setUp() throws Exception {
        member = MemberBuilder.build();
        memberRepository.deleteAllInBatch();
        refreshTokenRepository.deleteAll();
//        sessionRepository.deleteAllInBatch();
    }

    @MockBean
    private OauthHandler oauthHandler;

    @Test
    @DisplayName("PW를 검증하고, 로그인 처리를 한다.")
    public void signinWithPassword() {
        // given
        String encodedPassWord = passwordEncoder.encode(member.getPassword());
        Member savedMember = MemberBuilder.build(member.getEmail(), encodedPassWord);
        memberRepository.save(savedMember);

        final String email = savedMember.getEmail();
        final String password = member.getPassword();
        final SignInReq signInReq = SignInReqBuilder.build(email, password);

        // when
        BasicLoginResponse basicLoginResponse = authService.signIn(signInReq);

        // then
        assertThat(basicLoginResponse.getEmail()).isEqualTo(member.getEmail());
        assertThat(basicLoginResponse.getAccessToken()).isNotBlank();
        assertThat(basicLoginResponse.getRefreshToken()).isNotBlank();
    }

    @Test
    @DisplayName("존재하지않는 이메일로 로그인을 시도하면 예외를 던진다.")
    public void signinWithInvalidEmail() {
        // given
        final String email = member.getEmail();
        final String password = member.getPassword();
        final SignInReq signInReq = SignInReqBuilder.build(email, password);

        // when
        assertThatThrownBy(() -> authService.signIn(signInReq)).isInstanceOf(EmailNotFoundException.class);
    }

    @Test
    @DisplayName("유효하지않은 패스워드로 로그인을 시도하면 예외를 던진다.")
    public void signinWithInvalidPassword() {
        // given
        memberRepository.save(member);
        final String email = member.getEmail();
        final String password = "wrong";
        final SignInReq signInReq = SignInReqBuilder.build(email, password);

        // when
        assertThatThrownBy(() -> authService.signIn(signInReq)).isInstanceOf(PasswordInvalidException.class);
    }

    @Test
    @DisplayName("oauth 로그인 처리를 한다.")
    public void oauthSighin() {
        // given
        memberRepository.save(member);
        OauthUserInformation oauthUserInformation = OauthUserInformation.builder().email(member.getEmail()).build();
        given(oauthHandler.getUserInfoFromAuthCode("github", "accessToken")).willReturn(oauthUserInformation);

        // when
        OAuthLoginResponse oAuthLoginResponse = authService.oauthSignIn("github", "accessToken");

        // then
        assertThat(oAuthLoginResponse.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원가입이 되어있지 않은 email로 oauth 로그인 시도하면, 예외를 던진다.")
    public void Oauth_로그인_실패() {
        //given
        OauthUserInformation oauthUserInformation = OauthUserInformation.builder().email(member.getEmail()).build();
        given(oauthHandler.getUserInfoFromAuthCode("github", "accessToken")).willReturn(oauthUserInformation);

        // when then
        assertThatThrownBy(() -> authService.oauthSignIn("github", "accessToken")).isInstanceOf(EmailNotFoundException.class);
    }

    @Test
    @DisplayName("refresh 토큰을 검증하고, 새로운 access token을 발급한다.")
    public void refresh() {
        // given
        String encodedPassWord = passwordEncoder.encode(member.getPassword());
        Member savedMember = MemberBuilder.build(member.getEmail(), encodedPassWord);
        memberRepository.save(savedMember);

        final String email = savedMember.getEmail();
        final String password = member.getPassword();
        final SignInReq signInReq = SignInReqBuilder.build(email, password);

        BasicLoginResponse basicLoginResponse = authService.signIn(signInReq);

        // when
        RefreshAuthRequest request = RefreshAuthRequest.builder()
                .email(email)
                .refreshToken(basicLoginResponse.getRefreshToken())
                .build();

        RefreshAuthResponse refreshAuthResponse = authService.refreshAuth(request);

        // then
        assertThat(refreshAuthResponse.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("유효하지 않은 refresh token에 대한 요청이 들어오면, 예외를 던진다.")
    public void refreshWithInvalidRefreshToken() {
        // given
        String encodedPassWord = passwordEncoder.encode(member.getPassword());
        Member savedMember = MemberBuilder.build(member.getEmail(), encodedPassWord);
        memberRepository.save(savedMember);

        final String email = savedMember.getEmail();
        final String password = member.getPassword();
        final SignInReq signInReq = SignInReqBuilder.build(email, password);

        authService.signIn(signInReq);

        RefreshAuthRequest request = RefreshAuthRequest.builder()
                .email(email)
                .refreshToken("INVALID_REFRESH_TOKEN")
                .build();

        // when then
        assertThatThrownBy(() -> authService.refreshAuth(request)).isInstanceOf(InvalidRefreshToken.class);
    }

    @Test
    @DisplayName("유효하지 않은 email로 인증 refresh 요청이 들어오면, 예외를 던진다.")
    public void refreshWithInvalidEmail() {
        // given
        RefreshAuthRequest request = RefreshAuthRequest.builder()
                .email("invalid@email.com")
                .refreshToken("INVALID_REFRESH_TOKEN")
                .build();

        // when then
        assertThatThrownBy(() -> authService.refreshAuth(request)).isInstanceOf(TokenNotMatchedByEmail.class);
    }

    @Test
    @DisplayName("session을 생성한다.")
    public void session() {
        // when
        SessionDto sessionDto = authService.makeSession(null, "NEW_UUID");
        // then
        assertThat(sessionDto.getUuid()).isNotBlank();
    }

    @Test
    @DisplayName("세션이 존재하는 상황에서, session을 생성하면 에러를 던진다.")
    public void sessionWithNotNullId() {
        // when && then
        assertThatThrownBy(()-> authService.makeSession("OLD_UUID","NEW_UUID"))
                .isInstanceOf(AlreadySessionExist.class);
    }


    @Test
    @DisplayName("sessionId가 중복되면 에러를 던진다.")
    public void sessionWithDuplicatedId() {
        // given
        authService.makeSession(null,"DUPLICATED_UUID");

        // when && then
        assertThatThrownBy(()-> authService.makeSession(null,"DUPLICATED_UUID"))
                .isInstanceOf(DuplicatedSessionUUID.class);
    }
}