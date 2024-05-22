package com.example.account.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FoundUnregisteredAccountException extends RuntimeException {
    public FoundUnregisteredAccountException(String message) {
        super(message);
    }
}
