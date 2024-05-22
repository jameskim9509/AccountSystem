package com.example.account.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
}
