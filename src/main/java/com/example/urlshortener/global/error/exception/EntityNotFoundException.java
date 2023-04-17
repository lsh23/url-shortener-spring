package com.example.urlshortener.global.error.exception;

import com.example.urlshortener.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException{
    public EntityNotFoundException(String msg, ErrorCode errorCode) {
        super(msg, errorCode);
    }
}
