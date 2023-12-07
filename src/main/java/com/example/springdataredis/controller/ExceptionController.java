package com.example.springdataredis.controller;

import com.example.springdataredis.vo.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> NotFoundExceptionResponse(NotFoundException e) {
        return new ResponseEntity<>(e.getErrorMessage(), e.getHttpStatus());
    }
}
