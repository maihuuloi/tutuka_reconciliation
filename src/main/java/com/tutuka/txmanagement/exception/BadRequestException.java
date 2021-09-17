package com.tutuka.txmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class BadRequestException extends HttpStatusCodeException {

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String messageCode, String statusText) {
        super(HttpStatus.BAD_REQUEST, statusText);
    }

}
