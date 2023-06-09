package com.example.urlshortener.domain.auth.config;

import com.example.urlshortener.domain.auth.application.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    public AuthenticationConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> pathsToExclude = List.of(
                "/api/members/signup",
                "/api/auth/oauth",
                "/api/auth/signin",
                "/docs/index.html",
                "/favicon.ico"
        );

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns()
                .excludePathPatterns(pathsToExclude);
    }
}