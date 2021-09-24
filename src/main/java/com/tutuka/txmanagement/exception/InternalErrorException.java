package com.tutuka.txmanagement.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.nio.charset.Charset;

public class InternalErrorException extends HttpStatusCodeException {
    public InternalErrorException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public InternalErrorException(String statusText) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusText);
    }

    public InternalErrorException(String statusText, byte[] responseBody, Charset responseCharset) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusText, responseBody, responseCharset);
    }

    public InternalErrorException(String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusText, responseHeaders, responseBody, responseCharset);
    }

    public InternalErrorException(String message, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, statusText, responseHeaders, responseBody, responseCharset);
    }
}
