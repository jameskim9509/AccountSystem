package com.example.account.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotSameAccountNumberAndTransactionException extends RuntimeException {
    public NotSameAccountNumberAndTransactionException(String message) {
        super(message);
    }
}
