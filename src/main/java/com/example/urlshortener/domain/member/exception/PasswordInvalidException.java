package com.example.urlshortener.domain.member.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class PasswordInvalidException extends BusinessException {
    public PasswordInvalidException(){
        super(ErrorCode.PASSWORD_INVALID);
    }
}
