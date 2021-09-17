package com.tutuka.txmanagement.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.nio.charset.Charset;

public class InternalException extends HttpStatusCodeException {
    public InternalException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public InternalException(String statusText) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusText);
    }

    public InternalException(String statusText, byte[] responseBody, Charset responseCharset) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusText, responseBody, responseCharset);
    }

    public InternalException(String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusText, responseHeaders, responseBody, responseCharset);
    }

    public InternalException(String message, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, statusText, responseHeaders, responseBody, responseCharset);
    }
}
