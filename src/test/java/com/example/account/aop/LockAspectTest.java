package com.example.account.aop;

import com.example.account.dto.CreateTransactionForm;
import com.example.account.service.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LockAspectTest {
    @Mock
    RedisService redisService;

    @InjectMocks
    LockAspect lockAspect;

    @DisplayName("getLock 성공")
    @Test
    void getLockTest()
    {
        ArgumentCaptor<String> accountNumberCaptor
                = ArgumentCaptor.forClass(String.class);

        lockAspect.getLock(
                CreateTransactionForm.RequestForm.builder()
                        .accountNumber("1234")
                        .build()
        );

        verify(redisService, times(1)).getLock(accountNumberCaptor.capture());
        assertEquals("1234", accountNumberCaptor.getValue());
    }

    @DisplayName("releaseLock 성공")
    @Test
    void releaseLockTest()
    {
        ArgumentCaptor<String> accountNumberCaptor
                = ArgumentCaptor.forClass(String.class);

        lockAspect.releaseLock(
                CreateTransactionForm.RequestForm.builder()
                        .accountNumber("1234")
                        .build()
        );

        verify(redisService, times(1)).releaseLock(accountNumberCaptor.capture());
        assertEquals("1234", accountNumberCaptor.getValue());
    }
}
