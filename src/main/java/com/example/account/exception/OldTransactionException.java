package com.example.account.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OldTransactionException extends RuntimeException {
    public OldTransactionException(String message) {
        super(message);
    }
}
