package com.example.urlshortener.domain.auth.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class DuplicatedSessionUUID extends BusinessException {

    public DuplicatedSessionUUID(){
        super(ErrorCode.DUPLICATED_SESSION_UUID);
    }


}
