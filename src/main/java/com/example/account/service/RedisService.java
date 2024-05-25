package com.example.account.service;

import com.example.account.exception.AccountException;
import com.example.account.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedissonClient redissonClient;

    public String getLock(String accountNumber) {
        RLock lock = redissonClient.getLock(accountNumber);
        try {
            boolean isLock = lock.tryLock(1, 100, TimeUnit.SECONDS);
            if (!isLock) {
                return "Lock failed";
            }
        } catch (Exception e) {
            log.error("Redis lock failed");
            throw new AccountException(ErrorCode.IN_PROCESSING);
        }
        return "getLock";
    }

    public String releaseLock(String accountNumber) {
        RLock lock = redissonClient.getLock(accountNumber);
        lock.unlock();
        return "releaseLock";
    }

}
