package com.example.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(
        annotations = RestController.class, basePackages = "com.example.account.controller"
)
public class APIExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
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
    @ExceptionHandler(HttpMessageConversionException.class)
    public ErrorResponse handleHttpMessageConversionException(
            HttpMessageConversionException e
    )
    {
        return ErrorResponse.builder()
                .code(ErrorCode.JSON_PARSE_ERROR)
                .message(ErrorCode.JSON_PARSE_ERROR.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public List<ErrorResponse> handleBindException(BindException e)
    {
        List<FieldError> errors = e.getFieldErrors();
        return errors.stream().map(error -> ErrorResponse.builder()
                .code(ErrorCode.ARGUMENT_NOT_VALID)
                .message(error.getField() + " : " + error.getDefaultMessage())
                .build()).collect(Collectors.toList());
    }
}
