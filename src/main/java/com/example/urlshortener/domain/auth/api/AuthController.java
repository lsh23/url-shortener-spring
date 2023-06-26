package com.example.urlshortener.domain.auth.api;

import com.example.urlshortener.domain.auth.application.AuthService;
import com.example.urlshortener.domain.auth.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    final private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<BasicLoginResponse> signIn(@RequestBody final SignInReq signInReq) {
        return ResponseEntity.ok(authService.signIn(signInReq));
    }

    @GetMapping("/oauth")
    public ResponseEntity<OAuthLoginResponse> oauthSignIn(@RequestParam final String provider, @RequestParam String code) {
        return ResponseEntity.ok(authService.oauthSignIn(provider, code));
    }

    @GetMapping("/refresh")
    public ResponseEntity<RefreshAuthResponse> refreshAuth(@RequestBody final RefreshAuthRequest refreshTokenReq) {
        RefreshAuthResponse refreshAuthResponse = authService.refreshAuth(refreshTokenReq);
        return ResponseEntity.ok().body(refreshAuthResponse);
    }

    @GetMapping("/set-cookie")
    public ResponseEntity<Void> cookie(@CookieValue(value = "sessionId", required = false) String sessionId) {
        SessionDto sessionDto = authService.makeSession(sessionId, UUID.randomUUID().toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, sessionDto.toCookie().toString())
                .build();
    }

}
