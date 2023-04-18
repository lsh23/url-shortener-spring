package com.example.urlshortener.domain.member.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class EmailDuplicateException extends BusinessException {
    public EmailDuplicateException() {
        super(ErrorCode.EMAIL_DUPLICATION);
    }
}
