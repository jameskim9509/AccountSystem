package com.example.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class DeleteAccountForm {
    @Getter
    public static class RequestForm
    {
        @NotNull
        Long userId;
        @NotBlank
        String accountNumber;
    }

    @Builder
    @AllArgsConstructor
    public static class ResponseForm
    {
        Long userId;
        String accountNumber;
        LocalDateTime unRegisteredAt;
    }
}
