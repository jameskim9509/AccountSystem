package com.example.account.dto;

import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class GetTransactionForm {
    @Builder
    @Getter
    @AllArgsConstructor
//    @NoArgsConstructor
    public static class RequestForm
    {
        @NotBlank
        String transactionId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
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
