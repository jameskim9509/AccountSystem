package com.example.account.dto;

import com.example.account.domain.TransactionResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CancelTransactionForm {
    @Builder
    @Getter
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
    public static class ResponseForm
    {
        String accountNumber;
        TransactionResultType transactionResult;
        String transactionId;
        Long amount;
        LocalDateTime transactedAt;
    }
}
