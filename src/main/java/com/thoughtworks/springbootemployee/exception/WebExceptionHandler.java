package com.thoughtworks.springbootemployee.exception;

import org.omg.CORBA.portable.UnknownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class WebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UnknownCompanyException.class)
    public String unknownCompany(UnknownCompanyException exception) {
        log.info(exception.getMsg() + "[id == " + exception.getCompanyId() + "]");
        return exception.getMsg();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UnknownEmployeeException.class)
    public String unknownCompany(UnknownEmployeeException exception) {
        log.info(exception.getErrorMsg() + "[id == " + exception.getEmployeeId() + "]");
        return exception.getErrorMsg();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public @ResponseBody List<String> blandCompanyName(MethodArgumentNotValidException exception){
        return exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
    }

}

