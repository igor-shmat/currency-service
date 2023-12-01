package com.testapp.currencyservice.exception;

import com.testapp.currencyservice.model.ErrorResponse;
import org.springframework.http.HttpStatus;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleTodoNotFoundException(EntityNotFoundException exception) {
        return Mono.just(new ErrorResponse(exception.getMessage()));
    }
}
