package com.example.account.repository;

import com.example.account.domain.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AccountRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Account account)
    {
        em.persist(account);
        return account.getId();
    }

    public Account findById(Long accountId)
    {
        return em.find(Account.class, accountId);
    }

    public List<Account> findByUserId(Long userId)
    {
        String jpql = "select a from Account a join a.accountUser u on u.id=:userId";
        return em.createQuery(jpql, Account.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // 정책 예외처리 필요, 실패 응답 필요
    public Account findByUserIdAndAccountNumber(Long userId, String accountNumber)
    {
        String jpql = "select a from Account a join a.accountUser u on u.id=:userId and a.accountNumber=:accountNumber";
        return em.createQuery(jpql, Account.class)
            .setParameter("userId", userId)
            .setParameter("accountNumber", accountNumber)
            .getSingleResult();
    }
}