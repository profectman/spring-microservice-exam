package com.jeeho.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jeeho.common.security.serializer.CustomOauthExceptionSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {
    public CustomOauthException(String msg) {
        super(msg);
    }
}
