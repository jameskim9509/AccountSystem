package com.example.account.controller;

import com.example.account.aop.AccountLock;
import com.example.account.domain.Account;
import com.example.account.domain.Transaction;
import com.example.account.dto.*;
import com.example.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/account")
    public Object createAccount(
            @RequestBody @Valid CreateAccountForm.RequestForm reqForm
    )
    {
        Account createdAccount =
                accountService.createAccount(
                        reqForm.getUserId(),reqForm.getInitBalance()
                );
        return CreateAccountForm.ResponseForm.builder()
                .userId(createdAccount.getAccountUser().getId())
                .accountNumber(createdAccount.getAccountNumber())
                .registeredAt(createdAccount.getRegisteredAt())
                .build();
    }

    @DeleteMapping("/account")
    public Object unregisterAccount(
            @RequestBody @Valid DeleteAccountForm.RequestForm reqForm
    )
    {
        Account unregisteredAccount =
                accountService.unregisterAccount(
                        reqForm.getUserId(), reqForm.getAccountNumber()
                );
        return DeleteAccountForm.ResponseForm.builder()
                .userId(unregisteredAccount.getAccountUser().getId())
                .accountNumber(unregisteredAccount.getAccountNumber())
                .unRegisteredAt(unregisteredAccount.getUnregisteredAt())
                .build();
    }

    @GetMapping("/account")
    public List<Object> getAccounts(
            @ModelAttribute @Validated GetAccountForm.RequestForm reqForm
    )
    {
        List<Account> accountList =
                accountService.getAccountByUserId(reqForm.getUserId());
        return accountList.stream()
                .map(a -> GetAccountForm.ResponseForm.builder()
                        .accountNumber(a.getAccountNumber())
                        .balance(a.getBalance())
                        .build())
                .collect(Collectors.toList());
    }

    @AccountLock
    @PostMapping("/transaction/use")
    public Object createTransaction(
            @RequestBody @Valid CreateTransactionForm.RequestForm reqForm
    )
    {
        Transaction createdTransaction =
                accountService.createTransaction(
                        reqForm.getUserId(),
                        reqForm.getAccountNumber(),
                        reqForm.getAmount()
                );
        return CreateTransactionForm.ResponseForm.builder()
                .accountNumber(createdTransaction.getAccount().getAccountNumber())
                .transactionResult(createdTransaction.getTransactionResultType())
                .transactionId(createdTransaction.getTransactionId())
                .amount(createdTransaction.getAmount())
                .transactedAt(createdTransaction.getTransactedAt())
                .build();
    }

    @AccountLock
    @PostMapping("/transaction/cancel")
    public Object cancelTransaction(
            @RequestBody @Valid CancelTransactionForm.RequestForm reqForm
    )
    {
        Transaction canceledTransaction =
                accountService.cancelTransaction(
                        reqForm.getTransactionId(),
                        reqForm.getAccountNumber(),
                        reqForm.getAmount()
                );
        return CancelTransactionForm.ResponseForm.builder()
                .accountNumber(canceledTransaction.getAccount().getAccountNumber())
                .transactionResult(canceledTransaction.getTransactionResultType())
                .transactionId(canceledTransaction.getTransactionId())
                .amount(canceledTransaction.getAmount())
                .transactedAt(canceledTransaction.getTransactedAt())
                .build();
    }

    @GetMapping("/transaction/{transactionId}")
    public Object getTransaction(
            @ModelAttribute @Validated GetTransactionForm.RequestForm reqForm
    )
    {
        Transaction findTransaction =
                accountService.getTransaction(reqForm.getTransactionId());
        return GetTransactionForm.ResponseForm.builder()
                .accountNumber(findTransaction.getAccount().getAccountNumber())
                .transactionType(findTransaction.getTransactionType())
                .transactionResult(findTransaction.getTransactionResultType())
                .transactionId(findTransaction.getTransactionId())
                .amount(findTransaction.getAmount())
                .transactedAt(findTransaction.getTransactedAt())
                .build();
    }
}
