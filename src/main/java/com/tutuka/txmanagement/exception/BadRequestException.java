package com.tutuka.txmanagement.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {

    private String messageCode;

    public BadRequestException(String messageCode, String text) {
        super(text);
        this.messageCode = messageCode;
    }

    public BadRequestException(String messageCode, String text, Exception e) {
        super(text, e);
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }
}
