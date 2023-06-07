package com.example.urlshortener.domain.auth.api;

import com.example.urlshortener.domain.auth.application.AuthService;
import com.example.urlshortener.domain.auth.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
