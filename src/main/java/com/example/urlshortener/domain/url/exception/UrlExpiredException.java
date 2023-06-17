package com.example.urlshortener.domain.url.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class UrlExpiredException extends BusinessException {

    public UrlExpiredException() {
        super(ErrorCode.EXPIRED_URL);
    }
}
