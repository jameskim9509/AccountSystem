package com.example.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GetAccountForm {
    @Builder
    @Getter
    @AllArgsConstructor
//    @NoArgsConstructor
    public static class RequestForm
    {
        @NotNull @Min(1)
        Long userId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseForm
    {
        String accountNumber;
        Long balance;
    }
}
