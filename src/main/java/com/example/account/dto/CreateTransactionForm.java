package com.example.account.dto;

import com.example.account.type.TransactionResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateTransactionForm {
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
        @NotNull @Range(min = 1000, max = 500000)
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
