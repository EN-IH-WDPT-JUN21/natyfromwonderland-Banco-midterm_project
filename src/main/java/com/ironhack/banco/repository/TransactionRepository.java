package com.ironhack.banco.repository;

import com.ironhack.banco.dao.Money;
import com.ironhack.banco.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(Long aLong);
    List<Transaction> findAll();
    List<Transaction> findByTransactionTime (Timestamp transactionTime);
    List<Transaction> findByAccountIdAndTransactionTimeBetween(Long id, Timestamp start, Timestamp finish);

}
