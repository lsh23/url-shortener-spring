package com.example.urlshortener.domain.auth.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException(){
        super(ErrorCode.PASSWORD_INVALID);
    }
}
