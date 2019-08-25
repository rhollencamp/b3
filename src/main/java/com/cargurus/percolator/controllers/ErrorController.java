package com.cargurus.percolator.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {

    private final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception e) {
        log.error("Unexpected error", e);
        return new ModelAndView("err/500");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(MissingServletRequestParameterException e) {
        return new ModelAndView("err/400");
    }

    @ExceptionHandler
    public ModelAndView handleException(ResponseStatusException e) {
        switch (e.getStatus()) {
            case NOT_FOUND:
                return new ModelAndView("err/404", HttpStatus.NOT_FOUND);
            default:
                return new ModelAndView("err/500", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
