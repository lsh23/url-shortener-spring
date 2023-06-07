package com.example.urlshortener.domain.auth.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class TokenExpiredException extends BusinessException {
    public TokenExpiredException(){
        super(ErrorCode.EXPIRED_TOKEN);
    }
}
