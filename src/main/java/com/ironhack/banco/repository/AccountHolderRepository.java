package com.ironhack.banco.repository;

import com.ironhack.banco.dao.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHolderRepository extends JpaRepository <AccountHolder, Long> {
}
