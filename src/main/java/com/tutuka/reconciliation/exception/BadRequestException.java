package com.tutuka.reconciliation.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.nio.charset.Charset;

public class BadRequestException extends HttpStatusCodeException {

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String statusText) {
        super(HttpStatus.BAD_REQUEST, statusText);
    }

}
