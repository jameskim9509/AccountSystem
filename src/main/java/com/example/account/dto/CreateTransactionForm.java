package com.example.account.dto;

import com.example.account.domain.TransactionResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class CreateTransactionForm {
    @Getter
    public static class RequestForm
    {
        @NotNull
        Long userId;
        @NotBlank
        String accountNumber;
        @NotNull @Min(value = 1000)
        Long amount;
    }

    @Builder
    @AllArgsConstructor
    public static class ResponseForm
    {
        String accountNumber;
//        String type;
        TransactionResultType transactionResult;
        String transactionId;
        Long amount;
        LocalDateTime transactedAt;
    }
}
