package com.ironhack.banco.repository;

import com.ironhack.banco.dao.Account;
import com.ironhack.banco.dao.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
public interface SavingsRepository extends JpaRepository<Savings, Long> {
}
