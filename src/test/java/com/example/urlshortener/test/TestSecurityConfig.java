package com.example.urlshortener.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example.urlshortener.domain.auth.config")
public class TestSecurityConfig {
}
