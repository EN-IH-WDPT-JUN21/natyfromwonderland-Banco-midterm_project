package com.ironhack.banco.repository;

import com.ironhack.banco.dao.accounts.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(Long id);
    List<Transaction> findAll();
    List<Transaction> findBySenderId(Long id);
    List<Transaction> findByTransactionTime (Timestamp transactionTime);
    List<Transaction> findBySenderIdAndTransactionTimeBetween(Long id, Date start, Date finish);
}
