package com.example.urlshortener.domain.member.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class EmailNotFoundException extends BusinessException {
    public EmailNotFoundException(){
        super(ErrorCode.EMAIL_INVALID);
    }
}
