package com.example.urlshortener.domain.auth.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class AlreadySessionExist extends BusinessException {
    public AlreadySessionExist(){
        super(ErrorCode.ALREADY_SESSION_EXIST);
    }
}
