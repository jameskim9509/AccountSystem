package com.example.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GetAccountForm {
    @Getter
    public static class RequestForm
    {
        @NotNull @Min(1)
        Long userId;
    }

    @Builder
    @AllArgsConstructor
    public static class ResponseForm
    {
        String accountNumber;
        Long balance;
    }
}
