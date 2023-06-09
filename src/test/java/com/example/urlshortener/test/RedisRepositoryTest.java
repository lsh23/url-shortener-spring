package com.example.urlshortener.test;

import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataRedisTest
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("test")
@Disabled
public class RedisRepositoryTest {

}