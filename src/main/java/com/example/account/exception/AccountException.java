package com.example.account.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountException extends RuntimeException{
    private String message;
    private ErrorCode errorCode;

    public AccountException(ErrorCode errorCode)
    {
        super(errorCode.getMessage());
        this.message = errorCode.getMessage();
        this.errorCode = errorCode;
    }
}
