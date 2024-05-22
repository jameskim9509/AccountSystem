package com.example.account.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id @GeneratedValue(generator = "transactionGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResultType;

    @ManyToOne
    @JoinColumn(name="ACCOUNT_ID")
    private Account account;

    private Long amount;
    private Long balanceSnapshot;
    private String transactionId;
    private LocalDateTime transactedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
