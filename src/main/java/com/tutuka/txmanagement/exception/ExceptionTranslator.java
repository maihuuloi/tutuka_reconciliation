package com.tutuka.txmanagement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map handleBadRequestException(HttpServletRequest request, BadRequestException ex) {
        log.debug(ex.getMessage(), ex);

        Map map = new HashMap();
        map.put("code", ex.getMessageCode());
        map.put("message", ex.getMessage());

        return map;
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Map handleFileSizeLimitExceeded(MaxUploadSizeExceededException ex) {

        Map map = new HashMap();
        map.put("code", "file.limit.exceed");
        map.put("message", ex.getMessage());

        return map;
    }
}
