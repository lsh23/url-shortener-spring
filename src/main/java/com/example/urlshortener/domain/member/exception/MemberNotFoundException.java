package com.example.urlshortener.domain.member.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {
    public MemberNotFoundException(String msg) {
        super(msg, ErrorCode.ENTITY_NOT_FOUND);
    }
}
