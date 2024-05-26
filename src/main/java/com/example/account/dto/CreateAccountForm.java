package com.example.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateAccountForm {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestForm
    {
        @NotNull @Min(1)
        Long userId;
        @NotNull
        Long initBalance;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseForm
    {
        Long userId;
        String accountNumber;
        LocalDateTime registeredAt;
    }
}
