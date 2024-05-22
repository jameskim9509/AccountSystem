package com.example.account.repository;

import com.example.account.domain.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TransactionRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(Transaction transaction) {
        em.persist(transaction);
        return transaction.getId();
    }

    public Transaction findTransactionByUserIdAndAccountNumberAndAmount(Long userId, String accountNumber, Long amount)
    {
        String jpql = "select t from Transaction t join t.account a on a.id=:userId and a.accountNumber=:accountNumber where t.amount=:amount";
        return em.createQuery(jpql, Transaction.class)
                .setParameter("userId", userId)
                .setParameter("accountNumber", accountNumber)
                .setParameter("amount", amount)
                .getSingleResult();
    }

    public Transaction findByTransactionId(String transactionId)
    {
        String jpql = "select t from Transaction t where t.transactionId=:transactionId";
        return em.createQuery(jpql, Transaction.class)
                .setParameter("transactionId", transactionId)
                .getSingleResult();
    }
}
