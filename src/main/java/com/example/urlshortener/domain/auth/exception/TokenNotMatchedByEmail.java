package com.example.urlshortener.domain.auth.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class TokenNotMatchedByEmail extends BusinessException {
    public TokenNotMatchedByEmail(){
        super(ErrorCode.TOKEN_NOT_MATCHED_BY_EMAIL);
    }
}
