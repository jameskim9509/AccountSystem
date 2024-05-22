package com.example.account.repository;

import com.example.account.domain.AccountUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AccountUserRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(AccountUser accountUser)
    {
        em.persist(accountUser);
        return accountUser.getId();
    }

    public AccountUser findById(Long userId)
    {
        return em.find(AccountUser.class, userId);
    }
}
