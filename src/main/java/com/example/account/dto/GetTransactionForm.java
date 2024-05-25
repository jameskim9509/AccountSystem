package com.example.account.dto;

import com.example.account.domain.TransactionResultType;
import com.example.account.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class GetTransactionForm {
    @Builder
    @Getter
    public static class RequestForm
    {
        @NotBlank
        String transactionId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResponseForm
    {
        String accountNumber;
        TransactionType transactionType;
        TransactionResultType transactionResult;
        String transactionId;
        Long amount;
        LocalDateTime transactedAt;
    }
}
