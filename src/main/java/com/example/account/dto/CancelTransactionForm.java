package com.example.account.dto;

import com.example.account.type.TransactionResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CancelTransactionForm {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestForm
    {
        @NotBlank
        String transactionId;
        @NotBlank
        String accountNumber;
        @NotNull @Min(value = 1000)
        Long amount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseForm
    {
        String accountNumber;
        TransactionResultType transactionResult;
        String transactionId;
        Long amount;
        LocalDateTime transactedAt;
    }
}
