package com.example.account.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FoundBalanceException extends RuntimeException {
    public FoundBalanceException(String message) {
        super(message);
    }
}
