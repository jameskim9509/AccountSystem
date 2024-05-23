package com.example.account.service;

import com.example.account.domain.*;
import com.example.account.exception.*;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;
    private final TransactionRepository transactionRepository;

    //--------------------------------------Account----------------------------------------------------
    @Transactional
    public Account createAccount(Long userId, Long initBalance) {
        //Refactoring 필요
        AccountUser accountUser = accountUserRepository.findById(userId);
        if(accountUser == null) throw new AccountException(ErrorCode.USER_NOT_FOUND);

        if(getAccountByUserId(userId).size() >= 10)
            throw new AccountException(ErrorCode.FULL_ACCOUNT);

        Account account = Account.builder()
                .accountUser(accountUser)
                .accountNumber(generateAccountNumber())
                .accountStatus(AccountStatus.IN_USE)
                .balance(initBalance)
                .registeredAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return accountRepository.save(account);
    }

//    @Transactional
//    public Account getAccount(Long accountId)
//    {
//        return accountRepository.findById(accountId);
//    }

    @Transactional
    public List<Account> getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream().filter(a -> a.getAccountStatus()==AccountStatus.IN_USE).collect(Collectors.toList());
    }

    @Transactional
    public Account unregisterAccount(Long userId, String accountNumber)
    {
        AccountUser findUser = accountUserRepository.findById(userId);
        if(findUser == null) throw new AccountException(ErrorCode.USER_NOT_FOUND);

        List<Account> accountList = accountRepository.findByUserIdAndAccountNumber(userId, accountNumber);
        if(accountList.size() == 0) throw new AccountException(ErrorCode.ACCOUNT_NOT_FOUND);

        Account findAccount = accountList.get(0);
        if(findAccount.getAccountStatus() == AccountStatus.UNREGISTERED)
            throw new AccountException(ErrorCode.CANCELED_TRANSACTION);

        if(findAccount.getBalance() > 0) throw new AccountException(ErrorCode.FOUND_BALANCE);

        findAccount.setAccountStatus(AccountStatus.UNREGISTERED);
        findAccount.setUnregisteredAt(LocalDateTime.now());
        findAccount.setUpdatedAt(LocalDateTime.now());
        return findAccount;
    }

    //------------------------------ Transaction ---------------------------------------------------
    // 에러 처리 필요, 정책 구현 필요
    @Transactional
    public Transaction getTransaction(String transactionId)
    {
        List<Transaction> transactionList = transactionRepository.findByTransactionId(transactionId);
        if(transactionList.size() == 0)
            throw new AccountException(ErrorCode.TRANSACTION_NOT_FOUND);

        return transactionList.get(0);
    }

    // 에러 처리 필요, 정책 구현 필요
    @Transactional
    public Transaction createTransaction(Long userId, String accountNumber, Long amount)
    {
        AccountUser findUser = accountUserRepository.findById(userId);
        if(findUser == null) throw new AccountException(ErrorCode.USER_NOT_FOUND);

        List<Account> accountList = accountRepository.findByUserIdAndAccountNumber(userId, accountNumber);
        if(accountList.size() == 0) throw new AccountException(ErrorCode.ACCOUNT_NOT_FOUND);

        Account findAccount = accountList.get(0);
        if(findAccount.getAccountStatus() == AccountStatus.UNREGISTERED)
            throw new AccountException(ErrorCode.UNREGISTERED_ACCOUNT);

        if(findAccount.getBalance() < amount)
            throw new AccountException(ErrorCode.NOT_ENOUGH_BALANCE);

        findAccount.setBalance(findAccount.getBalance() - amount);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.USE)
                .transactionResultType(TransactionResultType.S)
                .account(findAccount)
                .amount(amount)
                .balanceSnapshot(findAccount.getBalance() + amount)
                .transactionId(generateUUID())
                .transactedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        return transaction;
    }

    @Transactional
    public Transaction cancelTransaction(String transactionId, String accountNumber, Long amount)
    {
        List<Account> accountList = accountRepository.findByAccountNumber(accountNumber);
        if(accountList.size() == 0)
            throw new AccountException(ErrorCode.ACCOUNT_NOT_FOUND);
        Account findAccount = accountList.get(0);

        Transaction findTransaction = getTransaction(transactionId);
        System.out.println(findTransaction.getAmount());
        System.out.println(findTransaction.getAmount() - amount);
        if(findTransaction.getAccount().getAccountNumber() != findAccount.getAccountNumber())
            throw new AccountException(ErrorCode.NOT_MATCH_ACCOUNT_AND_TRANSACTION);
        else if(findTransaction.getAmount() != amount.longValue())
            throw new AccountException(ErrorCode.NOT_MATCH_AMOUNT);
        else if(findTransaction.getTransactedAt().isBefore(LocalDateTime.now().minusYears(1)))
            throw new AccountException(ErrorCode.OLD_TRANSACTION);
        else if(findTransaction.getTransactionType() == TransactionType.CANCEL)
            throw new AccountException(ErrorCode.CANCELED_TRANSACTION);

        findAccount.setBalance(findAccount.getBalance() + findTransaction.getAmount());
        findTransaction.setTransactionType(TransactionType.CANCEL);
        findTransaction.setUpdatedAt(LocalDateTime.now());
        return findTransaction;
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
