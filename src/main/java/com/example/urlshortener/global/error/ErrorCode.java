package com.example.urlshortener.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "이미 등록된 이메일입니다."),
    EMAIL_INVALID(400, "M002", "등록되지 않은 이메일입니다."),
    PASSWORD_INVALID(400, "M003", "잘못된 패스워드 입니다."),

    // OAuth
    TOKEN_NOT_MATCHED_BY_EMAIL(400, "O001", "이메일에 해당하는 refresh token 이 존재하지 않습니다."),

    INVALID_REFRESH_TOKEN(400, "O002", "유효하지 않은 refresh token 입니다."),
    INVALID_TOKEN(400, "O003", "유효하지 않은 token 입니다."),
    EXPIRED_TOKEN(400,"O004","만료된 token 입니다."),
    FETCH_ACCESS_TOKEN(500, "O005","oauth login에 실패했습니다."),

    // URL
    INVALID_HASH(400, "U001", "hash 값에 대응 되는 URL이 존재하지 않습니다."),
    EXPIRED_URL(400, "U002", "해당 url은 만료된 url입니다.");

    private int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
