package com.example.account.repository;

import com.example.account.domain.Account;
import com.example.account.domain.AccountStatus;
import com.example.account.domain.AccountUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
class AccountRepositoryTest {
    @Autowired AccountRepository repository;

//    @Test
//    @Transactional
//    public void testAccountSave()
//    {
//        Account account = Account.builder()
//                .accountNumber("40000")
//                .accountStatus(AccountStatus.IN_USE)
//                .build();
//
//        Long findId = repository.save(account);
//        Account findAccount = repository.findById(findId);
//
//        Assertions.assertThat(findAccount).isEqualTo(account);
//    }
//
//    @Test
//    @Transactional
//    public void testDeleteAccount()
//    {
//        AccountUser user1 = AccountUser.builder()
//                .name("user1")
//                .build();
//
//        Account account1 = Account.builder()
//                .accountUser(user1)
//                .accountNumber("1")
//                .accountStatus(AccountStatus.IN_USE)
//                .balance(0L)
//                .registeredAt(LocalDateTime.now())
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        Account account2 = Account.builder()
//                .accountUser(user1)
//                .accountNumber("2")
//                .accountStatus(AccountStatus.IN_USE)
//                .balance(0L)
//                .registeredAt(LocalDateTime.now())
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        repository.save(account1);
//        repository.save(account2);
//
//
//        repository.deleteByUserIdAndAccountNumber(account1.getId())
//    }

}