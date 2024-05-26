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
            boolean isLock =
                    lock.tryLock(1, 100, TimeUnit.SECONDS);
            if (!isLock) {
                throw new AccountException(ErrorCode.IN_PROCESSING);
            }
        } catch (AccountException e) {
            log.error("Lock failed");
            throw e;
        }
        catch (Exception e)
        {
            log.error("try_lock Exception");
        }
        return "getLock";
    }

    public String releaseLock(String accountNumber) {
        RLock lock = redissonClient.getLock(accountNumber);
        lock.unlock();
        return "releaseLock";
    }

}
