package com.example.urlshortener.domain.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null){
            throw new RuntimeException();
        }

        if (authorizationHeader.startsWith("Bearer") == false){
            throw new RuntimeException();
        }

        String jwtToken = authorizationHeader.split(" ")[1];
        jwtUtils.validateToken(jwtToken);

        return true;
    }
}
