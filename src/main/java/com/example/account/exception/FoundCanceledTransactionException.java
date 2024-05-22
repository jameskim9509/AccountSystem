package com.example.account.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FoundCanceledTransactionException extends RuntimeException {
    public FoundCanceledTransactionException(String message) {
        super(message);
    }
}
