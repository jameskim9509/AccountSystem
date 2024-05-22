package com.example.account.controller;

import com.example.account.domain.Account;
import com.example.account.domain.Transaction;
import com.example.account.dto.*;
import com.example.account.service.AccountService;
import com.example.account.service.RedisTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final RedisTestService redisTestService;

    @GetMapping("/get-lock")
    public String getLock() {
        return redisTestService.getLock();
    }

    @PostMapping("/account")
    public Object createAccount(@RequestBody @Valid CreateAccountForm.RequestForm reqForm)
    {
        Long accountId = accountService.createAccount(reqForm.getUserId(),reqForm.getInitBalance());
        Account createdAccount = accountService.getAccount(accountId);
        return CreateAccountForm.ResponseForm.builder()
                .userId(createdAccount.getAccountUser().getId())
                .accountNumber(createdAccount.getAccountNumber())
                .registeredAt(createdAccount.getRegisteredAt())
                .build();
    }

    @DeleteMapping("/account")
    public Object unregisterAccount(@RequestBody @Valid DeleteAccountForm.RequestForm reqForm)
    {
        Account unregisteredAccount = accountService.unregisterAccount(reqForm.getUserId(), reqForm.getAccountNumber());
        return DeleteAccountForm.ResponseForm.builder()
                .userId(unregisteredAccount.getAccountUser().getId())
                .accountNumber(unregisteredAccount.getAccountNumber())
                .unRegisteredAt(unregisteredAccount.getUnregisteredAt())
                .build();
    }

    @GetMapping("/account")
    public List<Object> getAccounts(@Valid @ModelAttribute GetAccountForm.RequestForm reqForm)
    {
        List<Account> accountList = accountService.getAccountByUserId(reqForm.getUserId());
        return accountList.stream()
                .map(a -> GetAccountForm.ResponseForm.builder()
                        .accountNumber(a.getAccountNumber())
                        .balance(a.getBalance())
                        .build())
                .collect(Collectors.toList());
    }

    @PostMapping("/transaction/use")
    public Object createTransaction(@RequestBody @Valid CreateTransactionForm.RequestForm reqForm)
    {
        Transaction createdTransaction = accountService.createTransaction(reqForm.getUserId(), reqForm.getAccountNumber(), reqForm.getAmount());
        return CreateTransactionForm.ResponseForm.builder()
                .accountNumber(createdTransaction.getAccount().getAccountNumber())
                .transactionResult(createdTransaction.getTransactionResultType())
                .transactionId(createdTransaction.getTransactionId())
                .amount(createdTransaction.getAmount())
                .transactedAt(createdTransaction.getTransactedAt())
                .build();
    }

    @PostMapping("/transaction/cancel")
    public Object cancelTransaction(@RequestBody @Valid CancelTransactionForm.RequestForm reqForm)
    {
        Transaction canceledTransaction = accountService.cancelTransaction(reqForm.getTransactionId(), reqForm.getAccountNumber(), reqForm.getAmount());
        return CancelTransactionForm.ResponseForm.builder()
                .transactionId(canceledTransaction.getTransactionId())
                .accountNumber(canceledTransaction.getAccount().getAccountNumber())
                .amount(canceledTransaction.getAmount())
                .build();
    }

    @GetMapping("/transaction")
    public Object getTransaction(@ModelAttribute @Valid GetTransactionForm.RequestForm reqForm)
    {
        Transaction findTransaction = accountService.getTransaction(reqForm.getTransactionId());
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
