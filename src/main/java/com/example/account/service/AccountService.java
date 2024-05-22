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
    public Long createAccount(Long userId, Long initBalance) {
        AccountUser accountUser = accountUserRepository.findById(userId);
        if(accountUser == null) throw new UserNotFoundException("사용자가 없습니다.");

        if(getAccountByUserId(userId).size() > 10)
            throw new FullAccountException("보유 가능 계좌의 수를 초과했습니다.");

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

    @Transactional
    public Account getAccount(Long accountId)
    {
        return accountRepository.findById(accountId);
    }

    @Transactional
    public List<Account> getAccountByUserId(Long userId) {
        if(userId < 0){
            throw new RuntimeException("Minus");
        }

        AccountUser accountUser = accountUserRepository.findById(userId);
        if(accountUser == null) throw new UserNotFoundException("사용자가 없습니다.");

        return accountRepository.findByUserId(userId)
                .stream().filter(a -> a.getAccountStatus()==AccountStatus.IN_USE).collect(Collectors.toList());
    }

    @Transactional
    public Account unregisterAccount(Long userId, String accountNumber)
    {
        AccountUser findUser = accountUserRepository.findById(userId);
        if(findUser == null) throw new UserNotFoundException("사용자가 없습니다.");

        List<Account> accountList = accountRepository.findByUserIdAndAccountNumber(userId, accountNumber);
        if(accountList.size() == 0) throw new AccountNotFoundException("해당하는 계좌가 없습니다.");

        Account findAccount = accountList.get(0);
        if(findAccount.getAccountStatus() == AccountStatus.UNREGISTERED)
            throw new FoundUnregisteredAccountException("이미 해지된 계좌입니다.");

        if(findAccount.getBalance() > 0) throw new FoundBalanceException("해당 계좌에 잔액이 존재합니다.");

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
            throw new TransactionNotFoundException("해당하는 거래가 없습니다.");

        return transactionList.get(0);
    }

    // 에러 처리 필요, 정책 구현 필요
    @Transactional
    public Transaction createTransaction(Long userId, String accountNumber, Long amount)
    {
        AccountUser findUser = accountUserRepository.findById(userId);
        if(findUser == null) throw new UserNotFoundException("사용자가 없습니다.");

        List<Account> accountList = accountRepository.findByUserIdAndAccountNumber(userId, accountNumber);
        if(accountList.size() == 0) throw new AccountNotFoundException("해당하는 계좌가 없습니다.");

        Account findAccount = accountList.get(0);
        if(findAccount.getAccountStatus() == AccountStatus.UNREGISTERED)
            throw new FoundUnregisteredAccountException("이미 해지된 계좌입니다.");

        if(findAccount.getBalance() < amount)
            throw new NotEnoughBalanceException("잔액이 부족합니다.");

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
                .updatedAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        return transaction;
    }

    @Transactional
    public Transaction cancelTransaction(String transactionId, String accountNumber, Long Amount)
    {
        List<Account> accountList = accountRepository.findByAccountNumber(accountNumber);
        if(accountList.size() == 0)
            throw new AccountNotFoundException("해당하는 계좌가 없습니다");
        Account findAccount = accountList.get(0);

        Transaction findTransaction = getTransaction(transactionId);
        if(findTransaction.getAccount().getAccountNumber() != findAccount.getAccountNumber())
            throw new NotSameAccountNumberAndTransactionException("계좌와 거래가 일치하지 않습니다.");
        else if(findTransaction.getAmount() != Amount)
            throw new NotSameAmountException("거래 취소 금액이 일치하지 않습니다.");
        else if(findTransaction.getTransactedAt().isBefore(LocalDateTime.now().minusYears(1)))
            throw new OldTransactionException("1년이 지난 거래입니다.");
        else if(findTransaction.getTransactionType() == TransactionType.CANCEL)
            throw new FoundCanceledTransactionException("이미 취소된 결제입니다.");

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
