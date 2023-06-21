package com.example.urlshortener.domain.url.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

import java.time.LocalDateTime;

public class InvalidProlongExpirationPeriodException extends BusinessException {


    public InvalidProlongExpirationPeriodException(LocalDateTime localDateTime) {
        super(ErrorCode.INVALID_PROLONG_EXPIRATION_PERIOD.getMessage() + ":" + localDateTime.toString(), ErrorCode.INVALID_PROLONG_EXPIRATION_PERIOD);
    }
}
