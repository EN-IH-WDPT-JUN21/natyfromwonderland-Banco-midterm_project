package com.ironhack.banco.repository;

import com.ironhack.banco.dao.accounts.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {
}
