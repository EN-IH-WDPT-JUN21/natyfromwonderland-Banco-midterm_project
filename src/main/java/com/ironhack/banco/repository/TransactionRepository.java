package com.ironhack.banco.repository;

import com.ironhack.banco.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(Long aLong);
    List<Transaction> findAll();
    List<Transaction> findByTransactionTime (Timestamp time);
}
