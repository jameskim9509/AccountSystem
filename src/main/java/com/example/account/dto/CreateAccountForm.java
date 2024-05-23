package com.example.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateAccountForm {
    @Getter
    public static class RequestForm
    {
        @NotNull @Min(1)
        Long userId;
        @NotNull
        Long initBalance;
    }

    @Builder
    @AllArgsConstructor
    public static class ResponseForm
    {
        Long userId;
        String accountNumber;
        LocalDateTime registeredAt;
    }
}
