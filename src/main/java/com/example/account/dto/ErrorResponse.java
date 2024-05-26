package com.example.account.dto;

import com.example.account.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private ErrorCode code;
    private String message;
}
