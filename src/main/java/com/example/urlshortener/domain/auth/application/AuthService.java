package com.example.urlshortener.domain.auth.application;

import com.example.urlshortener.domain.auth.dao.RefreshTokenRepository;
import com.example.urlshortener.domain.auth.domain.RefreshToken;
import com.example.urlshortener.domain.auth.dto.*;
import com.example.urlshortener.domain.auth.exception.InvalidRefreshToken;
import com.example.urlshortener.domain.auth.exception.TokenNotMatchedByEmail;
import com.example.urlshortener.domain.member.dao.MemberRepository;
import com.example.urlshortener.domain.member.domain.Member;
import com.example.urlshortener.domain.member.exception.EmailNotFoundException;
import com.example.urlshortener.domain.member.exception.PasswordInvalidException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final OauthHandler oauthHandler;
    private final JwtUtils jwtUtils;

    @Transactional
    public BasicLoginResponse signIn(SignInReq signInReq) {

        Member member = memberRepository.findByEmail(signInReq.getEmail())
                .orElseThrow(() -> new EmailNotFoundException());

        if (!checkPasswordMatch(signInReq.getPassword(), member)) {
            throw new PasswordInvalidException();
        }

        String token = issueToken(member);
        String refreshToken = issueRefreshToken(member);
        refreshTokenRepository.save(RefreshToken.of(member, refreshToken));

        return BasicLoginResponse.of(member, token, refreshToken);
    }

    private boolean checkPasswordMatch(String reqPassword, Member member) {
        return passwordEncoder.matches(reqPassword, member.getPassword());
    }

    public OAuthLoginResponse oauthSignIn(String provider, String code) {
        // access token 얻고 이메일 정보 가져오기
        OauthUserInformation userInfoFromAuthCode = oauthHandler.getUserInfoFromAuthCode(provider, code);
        String email = userInfoFromAuthCode.getEmail();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException());

        String token = issueToken(member);
        String refreshToken = issueRefreshToken(member);

        refreshTokenRepository.save(RefreshToken.of(member, refreshToken));

        return OAuthLoginResponse.of(member, token, refreshToken);
    }

    private String issueToken(final Member member) {
        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail(member.getEmail())
                .build();
        return jwtUtils.createToken(payload, new Date());
    }

    private String issueToken(final String email) {
        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail(email)
                .build();
        return jwtUtils.createToken(payload, new Date());
    }

    private String issueRefreshToken(final Member member) {
        Map<String, Object> payload = JwtUtils.payloadBuilder()
                .setUserEmail(member.getEmail())
                .build();
        return jwtUtils.createRefreshToken(payload, new Date());
    }

    public RefreshAuthResponse refreshAuth(RefreshAuthRequest refreshTokenReq) {

        String email = refreshTokenReq.getEmail();
        RefreshToken savedToken = refreshTokenRepository.findRefreshTokenByEmail(
                email).orElseThrow(() -> new TokenNotMatchedByEmail());

        if (!savedToken.getToken().equals(refreshTokenReq.getRefreshToken())) {
            throw new InvalidRefreshToken();
        }

        jwtUtils.validateRefreshToken(refreshTokenReq.getRefreshToken());

        String accessToken = issueToken(email);

        return RefreshAuthResponse.of(accessToken);
    }
}
