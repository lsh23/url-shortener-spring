package com.example.urlshortener.domain.auth.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class InvalidRefreshToken extends BusinessException {
    public InvalidRefreshToken(){
        super(ErrorCode.INVALID_TOKEN);
    }
}
