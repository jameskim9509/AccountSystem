package com.example.account.aop;

import com.example.account.dto.CancelTransactionForm;
import com.example.account.dto.CreateTransactionForm;
import com.example.account.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class LockAspect {
    private final RedisService redisService;

    @Pointcut("@annotation(com.example.account.aop.AccountLock)")
    public void lockPointCut() {}

    @Before("lockPointCut() && args(reqForm,..)")
    public void getLock(Object reqForm)
    {
        String accountNumber = "";
        if(reqForm instanceof CreateTransactionForm.RequestForm)
            accountNumber =
                    ((CreateTransactionForm.RequestForm) reqForm).getAccountNumber();
        else if(reqForm instanceof CancelTransactionForm.RequestForm)
            accountNumber =
                    ((CancelTransactionForm.RequestForm) reqForm).getAccountNumber();
        else return;

        log.info("redisService : {}", redisService.getLock(accountNumber));
    }

    @After("lockPointCut() && args(reqForm,..)")
    public void releaseLock(Object reqForm)
    {
        String accountNumber;
        if(reqForm instanceof CreateTransactionForm.RequestForm)
            accountNumber =
                    ((CreateTransactionForm.RequestForm) reqForm).getAccountNumber();
        else if(reqForm instanceof CancelTransactionForm.RequestForm)
            accountNumber =
                    ((CancelTransactionForm.RequestForm) reqForm).getAccountNumber();
        else return;

        log.info("redisService : {}", redisService.releaseLock(accountNumber));
    }
}
