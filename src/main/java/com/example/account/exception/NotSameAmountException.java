package com.example.account.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotSameAmountException extends RuntimeException {
    public NotSameAmountException(String message) {
        super(message);
    }
}
