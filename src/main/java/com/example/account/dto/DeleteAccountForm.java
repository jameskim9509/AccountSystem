package com.example.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class DeleteAccountForm {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestForm
    {
        @NotNull @Min(1)
        Long userId;
        @NotBlank
        String accountNumber;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseForm
    {
        Long userId;
        String accountNumber;
        LocalDateTime unRegisteredAt;
    }
}
