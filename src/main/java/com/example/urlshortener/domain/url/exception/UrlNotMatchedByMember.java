package com.example.urlshortener.domain.url.exception;

import com.example.urlshortener.global.error.ErrorCode;
import com.example.urlshortener.global.error.exception.BusinessException;

public class UrlNotMatchedByMember extends BusinessException {

    public UrlNotMatchedByMember(){
        super(ErrorCode.URL_NOT_MATCHED_BY_MEMBER);
    }

}
