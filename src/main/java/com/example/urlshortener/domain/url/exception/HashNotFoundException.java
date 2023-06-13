package com.example.urlshortener.domain.url.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class HashNotFoundException extends BusinessException {

    public HashNotFoundException() {
        super(ErrorCode.INVALID_HASH);
    }
}
