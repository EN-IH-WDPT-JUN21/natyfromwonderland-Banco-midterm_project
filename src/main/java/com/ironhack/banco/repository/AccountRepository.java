package com.ironhack.banco.repository;

import com.ironhack.banco.dao.Account;
import com.ironhack.banco.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long aLong);
}
