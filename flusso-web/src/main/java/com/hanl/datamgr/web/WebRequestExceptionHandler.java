package com.hanl.datamgr.web;


import com.hanl.datamgr.exception.BusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Hanl
 * @date :2019/10/9
 * @desc:
 */
@ControllerAdvice
@Slf4j
public class WebRequestExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleBindException(BindException ex) {
        log.error("Web Request Failed Valid Exception", ex);
        List<String> defaultMsg = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return CommonResponse.buildWithException(defaultMsg);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleBindException(MethodArgumentNotValidException ex) {
        log.error("Web Request Failed Valid Exception", ex);
        List<String> defaultMsg = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return CommonResponse.buildWithException(defaultMsg);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleBindGetException(ConstraintViolationException ex) {
        log.error("Web Request Failed Valid Exception", ex);
        List<String> defaultMsg = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return CommonResponse.buildWithException(defaultMsg);
    }


    @ExceptionHandler(value = BusException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handBusException(BusException ex) {
        log.error("Web Request Failed Bus Exception", ex);
        return CommonResponse.buildWithException(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleException(Exception e) {
        log.error("Web Request Failed Exception", e);
        return CommonResponse.buildWithException("Request Failed.");
    }

}
