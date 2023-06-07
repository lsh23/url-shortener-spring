package com.example.urlshortener.domain.auth.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class FetchAccessTokenException extends BusinessException {
    public FetchAccessTokenException(String message){
        super(message, ErrorCode.FETCH_ACCESS_TOKEN);
    }

}
