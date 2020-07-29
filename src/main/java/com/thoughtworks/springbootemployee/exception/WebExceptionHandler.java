package com.thoughtworks.springbootemployee.exception;

import org.omg.CORBA.portable.UnknownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UnknownCompanyException.class)
    public void unknownCompany(UnknownCompanyException ex) {
        log.info(ex.getMsg()+"[id == "+ex.getCompanyId() + "]");
    }

}
