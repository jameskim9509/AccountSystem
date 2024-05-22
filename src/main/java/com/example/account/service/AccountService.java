package com.example.account.service;

import com.example.account.domain.*;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;
    private final TransactionRepository transactionRepository;

    //--------------------------------------Account----------------------------------------------------
    @Transactional
    public Long createAccount(Long userId, Long initBalance) {
        AccountUser accountUser = accountUserRepository.findById(userId);
        if(accountUser == null) return null;

        // 예외처리 추가 보정 필요
        if(accountRepository.findByUserId(userId).size() > 10) return null;

        Account account = Account.builder()
                .accountUser(accountUser)
                .accountNumber(generateAccountNumber())
                .accountStatus(AccountStatus.IN_USE)
                .balance(initBalance)
                .registeredAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        return accountRepository.save(account);
    }

    @Transactional
    public List<Account> getAccountByUserId(Long userId) {
        if(userId < 0){
            throw new RuntimeException("Minus");
        }
        return accountRepository.findByUserId(userId);
    }

    // 에러 처리 필요
    @Transactional
    public void deleteAccount(Long userId, String accountNumber)
    {
        Account findAccount = accountRepository.findByUserIdAndAccountNumber(userId, accountNumber);
        findAccount.setRegisteredAt(null);
    }

    //------------------------------ Transaction ---------------------------------------------------
    // 에러 처리 필요, 정책 구현 필요
    @Transactional
    public Transaction getTransaction(String transactionId)
    {
        return transactionRepository.findByTransactionId(transactionId);
    }

    // 에러 처리 필요, 정책 구현 필요
    @Transactional
    public void createTransaction(Long userId, String accountNumber, Long amount)
    {
        Account findAccount = accountRepository.findByUserIdAndAccountNumber(userId, accountNumber);
        findAccount.setBalance(findAccount.getBalance() - amount);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.USE)
                .transactionResultType(TransactionResultType.S)
                .account(findAccount)
                .amount(amount)
                .balanceSnapshot(findAccount.getBalance())
                .transactionId(generateUUID())
                .transactedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

    // 정책 구현 필요, 에러 필요
    @Transactional
    public void cancelTransaction(Long userId, String accountNumber, Long canceledAmount)
    {
        Transaction findTransaction = transactionRepository.findTransactionByUserIdAndAccountNumberAndAmount(
                userId, accountNumber, canceledAmount
        );
        Account findAccount = findTransaction.getAccount();
        findAccount.setBalance(findAccount.getBalance() + findTransaction.getAmount());
    }

    //---------------------------------------Sample Data------------------------------------------------
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initUserData()
    {
        log.info("UserData init");
        AccountUser user1 = AccountUser.builder()
                .name("user1")
                .build();
        AccountUser user2 = AccountUser.builder()
                .name("user2")
                .build();

        accountUserRepository.save(user1);
        accountUserRepository.save(user2);
    }

    //---------------------------------Private Function----------------------------------------------
    private String generateAccountNumber()
    {
        int leftLimit = Character.valueOf('0');
        int rightLimit = Character.valueOf('9'); // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            sb.append((char) randomLimitedInt);
        }
        return sb.toString();
    }

    private String generateUUID()
    {
        return UUID.randomUUID().toString();
    }
}
