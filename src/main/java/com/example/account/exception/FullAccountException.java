package com.example.account.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FullAccountException extends RuntimeException{
    public FullAccountException(String message)
    {
        super(message);
    }
}
