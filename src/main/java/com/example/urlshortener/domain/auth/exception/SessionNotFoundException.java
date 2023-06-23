package com.example.urlshortener.domain.auth.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class SessionNotFoundException extends BusinessException {
    public SessionNotFoundException(){
        super("존재하지 않는 세션 ID 입니다.",ErrorCode.ENTITY_NOT_FOUND);
    }
}
