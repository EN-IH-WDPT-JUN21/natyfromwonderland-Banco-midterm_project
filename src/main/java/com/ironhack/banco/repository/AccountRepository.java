package com.ironhack.banco.repository;

import com.ironhack.banco.dao.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long id);

}
