package com.example.account.dto;

import com.example.account.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private ErrorCode code;
    private String message;
}
