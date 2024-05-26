package com.example.account.service;

import com.example.account.exception.AccountException;
import com.example.account.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RedisServiceTest {
    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock lock;

    @InjectMocks
    private RedisService redisService;

    @DisplayName("lock 획득 성공")
    @Test
    void getLock() throws InterruptedException {
        given(redissonClient.getLock(any()))
                .willReturn(lock);
        given(lock.tryLock(anyLong(), anyLong(), any()))
                .willReturn(true);

        assertEquals(
                "getLock",
                redisService.getLock("1234")
        );
    }

    @DisplayName("lock 획득 실패 확인")
    @Test
    void getLockFailed() throws InterruptedException {
        given(redissonClient.getLock(any()))
                .willReturn(lock);
        given(lock.tryLock(anyLong(), anyLong(), any()))
                .willReturn(false);

        AccountException ex = assertThrows(
                AccountException.class,
                () -> redisService.getLock("1234")
        );
        assertEquals(ex.getErrorCode(), ErrorCode.IN_PROCESSING);
    }

    @DisplayName("lock 해제 성공")
    @Test
    void releaseLock() throws InterruptedException {
        given(redissonClient.getLock(any()))
                .willReturn(lock);

        assertEquals(
                "releaseLock",
                redisService.releaseLock("1234")
        );
    }
}
