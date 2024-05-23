package com.example.account.service;

import com.example.account.domain.*;
import com.example.account.exception.AccountException;
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
    void createAccountTest()
    {
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
        verify(accountUserRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).findByUserId(anyLong());
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        Account createdAccount = accountCaptor.getValue();
        assertEquals(createdAccount.getAccountUser(), expectedAccount.getAccountUser());
        assertEquals(createdAccount.getAccountStatus(), expectedAccount.getAccountStatus());
        assertEquals(createdAccount.getBalance(), expectedAccount.getBalance());
    }

    @Test
    @DisplayName("계좌 생성 USER_NOT_FOUND 확인")
    void UesrNotFoundExceptionTest()
    {
        //given
        given(accountUserRepository.findById(anyLong())).willReturn(null);

        assertThrows(AccountException.class, () -> accountService.createAccount(1L, 1000L));

        //then
        verify(accountUserRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(0)).findByUserId(anyLong());
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("계좌 생성 FULL_ACCOUNT 확인")
    void FullAccountExceptionTest()
    {
        //given
        AccountUser user1 = AccountUser.builder()
                .name("user1")
                .build();
        given(accountUserRepository.findById(anyLong())).willReturn(user1);
        given(accountRepository.findByUserId(anyLong())).willReturn(
                List.of(
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build()
                )
        );

        assertThrows(AccountException.class, () -> accountService.createAccount(1L, 1000L));

        //then
        verify(accountUserRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).findByUserId(anyLong());
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("계좌 해지 성공")
    void unregisterAccountTest()
    {
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
        given(accountRepository.findByUserIdAndAccountNumber(1L, "0123456789")).willReturn(List.of(account1));

        //when
        Account unregisteredAccount = accountService.unregisterAccount(1L, "0123456789");

        //then
        verify(accountUserRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).findByUserIdAndAccountNumber(anyLong(), any());

        assertEquals(unregisteredAccount.getAccountUser(), user1);
        assertEquals(unregisteredAccount.getAccountStatus(), AccountStatus.UNREGISTERED);
        assertEquals(unregisteredAccount.getAccountNumber(), "0123456789");
    }

    @Test
    void unregisterAccountExTest()
    {

    }

    @Test
    @DisplayName("계좌 조회 성공")
    void getAccountsTest()
    {
        AccountUser user1 = AccountUser.builder()
                .id(1L)
                .name("user1")
                .build();
        given(accountRepository.findByUserId(1L))
                .willReturn(List.of(
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.IN_USE).build(),
                        Account.builder().accountStatus(AccountStatus.UNREGISTERED).build()
                        )
                );

        List<Account> accountList = accountService.getAccountByUserId(1L);

        //then
        verify(accountRepository, times(1)).findByUserId(anyLong());
        assertEquals(accountList.size(), 2);
    }

    @Test
    void getAccountsExTest()
    {

    }

    @Test
    @DisplayName("잔액 사용 성공")
    void createTransactionTest()
    {
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
        given(accountRepository.findByUserIdAndAccountNumber(1L, "0123456789")).willReturn(List.of(account1));
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        //when
        accountService.createTransaction(1L, "0123456789", 1000L);

        //then
        verify(accountUserRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).findByUserIdAndAccountNumber(anyLong(), any());
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());
        Transaction createdTransaction = transactionCaptor.getValue();
        assertEquals(createdTransaction.getAccount(), account1);
        assertEquals(createdTransaction.getAmount(), 1000L);
        assertEquals(createdTransaction.getBalanceSnapshot(), 5000L);
        assertEquals(createdTransaction.getAccount().getBalance(), 4000L);
    }

    @Test
    void createTransactionExTest()
    {

    }

    @Test
    @DisplayName("잔액 사용 취소 성공")
    void cancelTransactionTest()
    {
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
        Transaction transaction1 = Transaction.builder()
                        .transactionId("abcdefghijklmn")
                        .transactionType(TransactionType.USE)
                        .transactionResultType(TransactionResultType.S)
                        .transactedAt(LocalDateTime.now())
                        .account(account1)
                        .amount(1000L)
                        .build();

        given(accountRepository.findByAccountNumber("0123456789")).willReturn(List.of(account1));
        given(transactionRepository.findByTransactionId("abcdefghijklmn")).willReturn(List.of(transaction1));

        //when
        Transaction canceledTransaction = accountService.cancelTransaction("abcdefghijklmn", "0123456789", 1000L);

        //then
        assertEquals(canceledTransaction.getTransactionId(), "abcdefghijklmn");
        assertEquals(canceledTransaction.getAccount(), account1);
        assertEquals(canceledTransaction.getTransactionType(), TransactionType.CANCEL);
        assertEquals(canceledTransaction.getAmount(), 1000L);
    }

    @Test
    void cancelTransactionExTest()
    {

    }

    @Test
    @DisplayName("거래 조회 성공")
    void getTransaction()
    {
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
        Transaction transaction1 = Transaction.builder()
                .transactionId("abcdefghijklmn")
                .transactionType(TransactionType.USE)
                .transactionResultType(TransactionResultType.S)
                .transactedAt(LocalDateTime.now())
                .account(account1)
                .amount(1000L)
                .build();
        given(transactionRepository.findByTransactionId("abcdefghijklmn")).willReturn(List.of(transaction1));

        //when
        Transaction findTransaction = accountService.getTransaction("abcdefghijklmn");

        //then
        assertEquals(findTransaction.getTransactionId(), "abcdefghijklmn");
        assertEquals(findTransaction.getAccount(), account1);
        assertEquals(findTransaction.getAmount(), 1000L);
        assertEquals(findTransaction.getTransactionResultType(), TransactionResultType.S);
    }

//    @Test
//    @DisplayName("계좌 조회 성공")
//    void testXXX() {
//        //given
//        given(accountRepository.findById(anyLong()))
//                .willReturn(Optional.of(Account.builder()
//                        .accountStatus(AccountStatus.UNREGISTERED)
//                        .accountNumber("65789").build()));
//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
//
//        //when
//        Account account = accountService.getAccount(4555L);
//
//        //then
//        verify(accountRepository, times(1)).findById(captor.capture());
//        verify(accountRepository, times(0)).save(any());
//        assertEquals(4555L, captor.getValue());
//        assertNotEquals(45515L, captor.getValue());
//        assertEquals("65789", account.getAccountNumber());
//        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
//    }
//
//    @Test
//    @DisplayName("계좌 조회 실패 - 음수로 조회")
//    void testFailedToSearchAccount() {
//        //given
//        //when
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> accountService.getAccount(-10L));
//
//        //then
//        assertEquals("Minus", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Test 이름 변경")
//    void testGetAccount() {
//        //given
//        given(accountRepository.findById(anyLong()))
//                .willReturn(Optional.of(Account.builder()
//                        .accountStatus(AccountStatus.UNREGISTERED)
//                        .accountNumber("65789").build()));
//
//        //when
//        Account account = accountService.getAccount(4555L);
//
//        //then
//        assertEquals("65789", account.getAccountNumber());
//        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
//    }
//
//    @Test
//    void testGetAccount2() {
//        //given
//        given(accountRepository.findById(anyLong()))
//                .willReturn(Optional.of(Account.builder()
//                        .accountStatus(AccountStatus.UNREGISTERED)
//                        .accountNumber("65789").build()));
//
//        //when
//        Account account = accountService.getAccount(4555L);
//
//        //then
//        assertEquals("65789", account.getAccountNumber());
//        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
//    }
}