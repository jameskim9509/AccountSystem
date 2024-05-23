package com.example.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(annotations = RestController.class, basePackages = "com.example.account.controller")
public class APIExceptionHandler {
    @ExceptionHandler(AccountException.class)
    public ErrorResponse handleAccountException(AccountException e)
    {
        return ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e)
    {
        return "";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorResponse> handleMethodNotValidException(MethodArgumentNotValidException e)
    {
        List<FieldError> errors = e.getFieldErrors();
        return errors.stream().map(error -> ErrorResponse.builder()
                .code(ErrorCode.ARGUMENT_NOT_VALID)
                .message(error.getField() + " : " + error.getDefaultMessage())
                .build()).collect(Collectors.toList());
    }
}
