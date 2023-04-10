package com.springboot.cloud.apiGateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApiGatewayException extends RuntimeException {
    public ApiGatewayException(String msg, Throwable t) {
        super(msg, t);
    }

    public ApiGatewayException(String msg) {
        super(msg);
    }
}
