package com.example.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.example.account.controller")
public class APIExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ErrorResponse handleAccountNotFoundException(AccountNotFoundException e)
    {
        return ErrorResponse.builder()
                .code("ACCOUNT_NOT_FOUND")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FoundBalanceException.class)
    public ErrorResponse handleFoundBalanceException(FoundBalanceException e)
    {
        return ErrorResponse.builder()
                .code("FOUND_BALANCE")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FoundCanceledTransactionException.class)
    public ErrorResponse handleCanceledTransactionException(FoundCanceledTransactionException e)
    {
        return ErrorResponse.builder()
                .code("CANCELED_TRANSACTION")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FoundUnregisteredAccountException.class)
    public ErrorResponse handleUnregisteredAccountException(FoundUnregisteredAccountException e)
    {
        return ErrorResponse.builder()
                .code("UNREGISTERED_ACCOUNT")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(FullAccountException.class)
    public ErrorResponse handleFullAccountException(FullAccountException e)
    {
        return ErrorResponse.builder()
                .code("FULL_ACCOUNT")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ErrorResponse handleNotEnoughBalanceException(NotEnoughBalanceException e)
    {
        return ErrorResponse.builder()
                .code("NOT_ENOUGH_BALANCE")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(NotSameAccountNumberAndTransactionException.class)
    public ErrorResponse handleNotSameAccountNumberAndTransactionException(NotSameAccountNumberAndTransactionException e)
    {
        return ErrorResponse.builder()
                .code("NOT_SAME_ACCOUNT_AND_TRANSACTION")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(NotSameAmountException.class)
    public ErrorResponse handleNotSameAmountException(NotSameAmountException e)
    {
        return ErrorResponse.builder()
                .code("NOT_SAME_AMOUNT")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(OldTransactionException.class)
    public ErrorResponse handleOldTransactionException(OldTransactionException e)
    {
        return ErrorResponse.builder()
                .code("OLD_TRANSACTION")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ErrorResponse handleTransactionNotFoundException(TransactionNotFoundException e)
    {
        return ErrorResponse.builder()
                .code("TRANSACTION_NOT_FOUND")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e)
    {
        return ErrorResponse.builder()
                .code("USER_NOT_FOUND")
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodNotValidException(MethodArgumentNotValidException e)
    {
        return ErrorResponse.builder()
                .code("ARGUMENT_NOT_VALID")
                .message(e.getMessage())
                .build();
    }
}
