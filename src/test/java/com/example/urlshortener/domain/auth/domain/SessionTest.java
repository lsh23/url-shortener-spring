package com.example.urlshortener.domain.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    @DisplayName("session에 uuid를 지정한다.")
    void assignUUID() {
        // given
        Session session = new Session();

        // when
        session.assignUUID("UUID");

        // then
        assertThat(session.getUUID()).isEqualTo("UUID");
    }
}