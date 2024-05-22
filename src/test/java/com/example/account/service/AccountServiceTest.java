package com.example.account.service;

import com.example.account.domain.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AccountServiceTest {
//    @Mock
//    private AccountRepository accountRepository;
//
//    @InjectMocks
//    private AccountService accountService;

    @Autowired private AccountService accountService;

    @Test
    void createAccountTest()
    {

    }

    @Test
    void createAccountExTest()
    {

    }

    @Test
    void deleteAccountTest()
    {
        Long accountId1 = accountService.createAccount(1L, 0L);
        Long accountId2 = accountService.createAccount(1L, 0L);

        Account findAccount = accountService.getAccount(accountId1);
        accountService.unregisterAccount(1L, findAccount.getAccountNumber());

        Assertions.assertThat(accountService.getAccount(accountId1)).isNull();
    }

    @Test
    void deleteAccountExTest()
    {

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