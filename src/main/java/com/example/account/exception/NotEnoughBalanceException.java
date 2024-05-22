package com.example.account.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException(String message) {
        super(message);
    }
}
