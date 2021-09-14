package com.ironhack.banco.repository;

import com.ironhack.banco.dao.Account;
import com.ironhack.banco.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
