package com.example.account.service;

import com.example.account.domain.*;
import com.example.account.exception.AccountException;
import com.example.account.exception.ErrorCode;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceMockTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountUserRepository accountUserRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

//    @BeforeEach
//    void dataInit()
//    {
//
//    }

    @Test
    @DisplayName("계좌 생성 성공")
    void createAccountTest() {
        //given
        Long initBalance = 1000L;
        Long userId = 1L;
        AccountUser user1 = AccountUser.builder()
                .name("user1")
                .build();
        Account expectedAccount = Account.builder()
                .accountUser(user1)
                .accountStatus(AccountStatus.IN_USE)
                .balance(initBalance)
                .build();
        given(accountUserRepository.findById(1L)).willReturn(user1);
        given(accountRepository.findByUserId(1L))
                .willReturn(new ArrayList<>());
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);

        //when
        accountService.createAccount(userId, initBalance);

        //then
        verify(accountUserRepository, times(2)).findById(anyLong());
        verify(accountRepository, times(1)).findByUserId(anyLong());
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        Account createdAccount = accountCaptor.getValue();
        assertEquals(createdAccount.getAccountUser(), expectedAccount.getAccountUser());
        assertEquals(createdAccount.getAccountStatus(), expectedAccount.getAccountStatus());
        assertEquals(createdAccount.getBalance(), expectedAccount.getBalance());
    }

    @Test
    @DisplayName("계좌 해지 성공")
    void unregisterAccountTest() {
        //given
        AccountUser user1 = AccountUser.builder()
                .name("user1")
                .build();
        Account account1 = Account.builder()
                .accountUser(user1)
                .accountNumber("0123456789")
                .balance(0L)
                .accountStatus(AccountStatus.IN_USE)
                .build();
        given(accountUserRepository.findById(1L)).willReturn(user1);
        given(accountRepository
                .findByUserIdAndAccountNumber(1L, "0123456789"))
                .willReturn(List.of(account1));

        //when
        Account unregisteredAccount = accountService
                .unregisterAccount(1L, "0123456789");

        //then
        verify(accountUserRepository, times(1))
                .findById(anyLong());
        verify(accountRepository, times(1))
                .findByUserIdAndAccountNumber(anyLong(), any());

        assertEquals(unregisteredAccount.getAccountStatus(), AccountStatus.UNREGISTERED);
    }

    @Test
    @DisplayName("계좌 조회 성공")
    void getAccountByUserIdTest() {
        AccountUser user1 = AccountUser.builder()
                .id(1L)
                .name("user1")
                .build();
        given(accountUserRepository.findById(1L))
                .willReturn(user1);
        given(accountRepository.findByUserId(1L))
                .willReturn(List.of(
                                Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                                Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                                Account.builder().accountStatus(AccountStatus.UNREGISTERED).build()
                        )
                );

        List<Account> accountList = accountService.getAccountByUserId(1L);

        //then
        verify(accountUserRepository, times(1)).findById(any());
        verify(accountRepository, times(1)).findByUserId(any());
        assertEquals(accountList.size(), 2);
    }

    @Test
    @DisplayName("잔액 사용 성공")
    void createTransactionTest() {
        AccountUser user1 = AccountUser.builder()
                .id(1L)
                .name("user1")
                .build();
        Account account1 = Account.builder()
                .accountUser(user1)
                .accountNumber("0123456789")
                .balance(5000L)
                .accountStatus(AccountStatus.IN_USE)
                .build();
        given(accountUserRepository.findById(1L)).willReturn(user1);
        given(accountRepository
                .findByUserIdAndAccountNumber(1L, "0123456789"))
                .willReturn(List.of(account1));
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        //when
        accountService.createTransaction(1L, "0123456789", 1000L);

        //then
        verify(accountUserRepository, times(1))
                .findById(anyLong());
        verify(accountRepository, times(1))
                .findByUserIdAndAccountNumber(anyLong(), any());
        verify(transactionRepository, times(1))
                .save(transactionCaptor.capture());

        Transaction createdTransaction = transactionCaptor.getValue();
        assertEquals(createdTransaction.getAmount(), 1000L);
        assertEquals(createdTransaction.getBalanceSnapshot(), 5000L);
        assertEquals(createdTransaction.getAccount().getBalance(), 4000L);
    }

    @Test
    @DisplayName("잔액 사용 취소 성공")
    void cancelTransactionTest() {
        AccountUser user1 = AccountUser.builder()
                .id(1L)
                .name("user1")
                .build();
        Account account1 = Account.builder()
                .accountUser(user1)
                .accountNumber("1234")
                .balance(5000L)
                .accountStatus(AccountStatus.IN_USE)
                .build();
        Transaction transaction1 = Transaction.builder()
                .transactionId("abcd")
                .transactionType(TransactionType.USE)
                .transactionResultType(TransactionResultType.S)
                .transactedAt(LocalDateTime.now())
                .account(account1)
                .amount(1000L)
                .build();

        given(accountRepository.findByAccountNumber("1234"))
                .willReturn(List.of(account1));
        given(transactionRepository.findByTransactionId("abcd"))
                .willReturn(List.of(transaction1));

        //when
        Transaction canceledTransaction = accountService
                .cancelTransaction("abcd", "1234", 1000L);

        //then
        verify(accountRepository, times(1))
                .findByAccountNumber(any());
        verify(transactionRepository, times(1))
                .findByTransactionId(any());

        assertEquals(canceledTransaction.getTransactionId(), "abcd");
        assertEquals(canceledTransaction.getAccount(), account1);
        assertEquals(canceledTransaction.getTransactionType(), TransactionType.CANCEL);
        assertEquals(canceledTransaction.getAmount(), 1000L);
    }

    @Test
    @DisplayName("거래 조회 성공")
    void getTransaction() {
        Transaction transaction1 = Transaction.builder()
                .transactionId("abcdefghijklmn")
                .transactionType(TransactionType.USE)
                .transactionResultType(TransactionResultType.S)
                .transactedAt(LocalDateTime.now())
                .amount(1000L)
                .build();
        given(transactionRepository
                .findByTransactionId("abcdefghijklmn"))
                .willReturn(List.of(transaction1));

        //when
        Transaction findTransaction = accountService
                .getTransaction("abcdefghijklmn");

        //then
        assertEquals(findTransaction.getTransactionId(), "abcdefghijklmn");
        assertEquals(findTransaction.getAmount(), 1000L);
        assertEquals(findTransaction.getTransactionResultType(), TransactionResultType.S);
    }

    @Test
    @DisplayName("USER_NOT_FOUND 확인")
    void UesrNotFoundExceptionTest() {
        //given
        given(accountUserRepository.findById(anyLong())).willReturn(null);

        AccountException ex =
                assertThrows(AccountException.class,
                        () -> accountService.createAccount(1L, 1000L));
        assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode());

        verify(accountUserRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("FULL_ACCOUNT 확인")
    void FullAccountExceptionTest() {
        //given
        AccountUser user1 = AccountUser.builder()
                .name("user1")
                .build();
        given(accountUserRepository.findById(anyLong())).willReturn(user1);
        given(accountRepository.findByUserId(anyLong())).willReturn(
                List.of(
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder()
                                .accountStatus(AccountStatus.IN_USE).build()
                )
        );

        AccountException ex = assertThrows(AccountException.class,
                () -> accountService
                        .createAccount(1L, 1000L));
        assertEquals(ErrorCode.FULL_ACCOUNT, ex.getErrorCode());
    }

    @DisplayName("ACCOUNT_NOT_FOUND 확인")
    @Test
    void AccountNotFoundExceptionTest() {
        given(accountRepository.findByAccountNumber(any()))
                .willReturn(new ArrayList<>());

        AccountException ex
                = assertThrows(AccountException.class,
                () -> accountService.getAccountByUserIdAndAccountNumber(null, "1234"));
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, ex.getErrorCode());
    }

    @DisplayName("UNREGISTERED_ACCOUNT 확인")
    @Test
    void UnregisteredAccountExceptionTest() {
        given(accountRepository.findByAccountNumber(any()))
                .willReturn(
                        List.of(
                                Account.builder()
                                    .accountStatus(AccountStatus.UNREGISTERED)
                                    .build()
                        )
                );

        AccountException ex = assertThrows(AccountException.class,
                () -> accountService.getAccountByUserIdAndAccountNumber(null,
                        "1234"));
        assertEquals(ErrorCode.UNREGISTERED_ACCOUNT, ex.getErrorCode());
    }

    @DisplayName("FOUND_BALANCE 확인")
    @Test
    void FoundBalanceExceptionTest()
    {
        given(accountUserRepository.findById(anyLong()))
                .willReturn(AccountUser.builder().build());
        given(accountRepository.findByUserId(anyLong()))
                .willReturn(
                        List.of(
                                Account.builder().build()
                        )
                );
        given(accountRepository.findByUserIdAndAccountNumber(any(), any()))
                .willReturn(
                        List.of(
                                Account.builder()
                                    .balance(1000L)
                                    .build()
                        )
                );

        AccountException ex = assertThrows(AccountException.class,
                () -> accountService.unregisterAccount(1L, "1234"));
        assertEquals(ErrorCode.FOUND_BALANCE, ex.getErrorCode());
    }

    @DisplayName("TRANSACTION_NOT_FOUND 확인")
    @Test
    void TransactionNotFoundExceptionTest()
    {
        given(transactionRepository.findByTransactionId(any()))
                .willReturn(new ArrayList<>());

        AccountException ex = assertThrows(AccountException.class,
                () -> accountService.getTransaction("1234"));
        assertEquals(ErrorCode.TRANSACTION_NOT_FOUND, ex.getErrorCode());
    }

    @DisplayName("NOT_ENOUGH_BALANCE 확인")
    @Test
    void NotEnoughBalanceTest()
    {
        given(accountUserRepository.findById(anyLong()))
                .willReturn(AccountUser.builder().build());
        given(accountRepository.findByUserId(anyLong()))
                .willReturn(
                        List.of(
                                Account.builder().build()
                        )
                );
        given(accountRepository.findByUserIdAndAccountNumber(any(), any()))
                .willReturn(
                        List.of(
                                Account.builder()
                                        .balance(0L)
                                        .build()
                        )
                );

        AccountException ex = assertThrows(AccountException.class,
                () -> accountService.createTransaction(1L,"1234", 1000L));
        assertEquals(ErrorCode.NOT_ENOUGH_BALANCE, ex.getErrorCode());
    }

    @DisplayName("NOT_MATCH_ACCOUNT_AND_TRANSACTION 확인")
    @Test
    void NotMachAccountAndTransactionExceptionTest()
    {
        given(accountRepository.findByAccountNumber(any()))
                .willReturn(
                        List.of(
                                Account.builder()
                                        .accountNumber("123456789")
                                        .build()
                        )
                );
        given(transactionRepository.findByTransactionId(any()))
                .willReturn(
                        List.of(
                                Transaction.builder()
                                        .account(
                                                Account.builder()
                                                        .accountNumber("1234")
                                                        .build()
                                        )
                                        .build()
                        )
                );

        AccountException ex = assertThrows(AccountException.class,
                () -> accountService.cancelTransaction("1234","123456", 1000L));
        assertEquals(ErrorCode.NOT_MATCH_ACCOUNT_AND_TRANSACTION, ex.getErrorCode());
    }

    @DisplayName("NOT_MATCH_AMOUNT 확인")
    @Test
    void NotMachAmountExceptionTest()
    {
        given(accountRepository.findByAccountNumber(any()))
                .willReturn(
                        List.of(
                                Account.builder()
                                        .accountNumber("1234")
                                        .build()
                        )
                );
        given(transactionRepository.findByTransactionId(any()))
                .willReturn(
                        List.of(
                                Transaction.builder()
                                        .account(
                                                Account.builder()
                                                        .accountNumber("1234")
                                                        .build()
                                        )
                                        .amount(2000L)
                                        .build()
                        )
                );

        AccountException ex = assertThrows(AccountException.class,
                () -> accountService.cancelTransaction("1234","123456", 1000L));
        assertEquals(ErrorCode.NOT_MATCH_AMOUNT, ex.getErrorCode());
    }

    @DisplayName("OLD_TRANSACTION 확인")
    @Test
    void OldTransactionExceptionTest()
    {
        given(accountRepository.findByAccountNumber(any()))
                .willReturn(
                        List.of(
                                Account.builder()
                                        .accountNumber("1234")
//                                        .balance(1000L)
                                        .build()
                        )
                );
        given(transactionRepository.findByTransactionId(any()))
                .willReturn(
                        List.of(
                                Transaction.builder()
                                        .account(
                                                Account.builder()
                                                        .accountNumber("1234")
                                                        .build()
                                        )
                                        .amount(1000L)
                                        .transactedAt(LocalDateTime.now().minusYears(2))
                                        .build()
                        )
                );

        AccountException ex = assertThrows(AccountException.class,
                () -> accountService.cancelTransaction("1234","123456", 1000L));
        assertEquals(ErrorCode.OLD_TRANSACTION, ex.getErrorCode());
    }

    @DisplayName("CANCELED_TRANSACTION 확인")
    @Test
    void CanceledTransactionTest()
    {
        given(accountRepository.findByAccountNumber(any()))
                .willReturn(
                        List.of(
                                Account.builder()
                                        .accountNumber("1234")
                                        .build()
                        )
                );
        given(transactionRepository.findByTransactionId(any()))
                .willReturn(
                        List.of(
                                Transaction.builder()
                                        .account(
                                                Account.builder()
                                                        .accountNumber("1234")
                                                        .build()
                                        )
                                        .amount(1000L)
                                        .transactedAt(LocalDateTime.now())
                                        .transactionType(TransactionType.CANCEL)
                                        .build()
                        )
                );

        AccountException ex = assertThrows(AccountException.class,
                () -> accountService.cancelTransaction("1234","123456", 1000L));
        assertEquals(ErrorCode.CANCELED_TRANSACTION, ex.getErrorCode());
    }
}