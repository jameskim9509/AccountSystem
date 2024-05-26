package com.example.account.repository;

import com.example.account.domain.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TransactionRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(Transaction transaction) {
        em.persist(transaction);
        return transaction.getId();
    }

    public List<Transaction> findByTransactionId(String transactionId)
    {
        String jpql = "select t " +
                "from Transaction t " +
                "where t.transactionId=:transactionId";
        return em.createQuery(jpql, Transaction.class)
                .setParameter("transactionId", transactionId)
                .getResultList();
    }
}
