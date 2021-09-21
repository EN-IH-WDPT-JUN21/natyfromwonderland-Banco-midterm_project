package com.ironhack.banco.repository;

import com.ironhack.banco.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface AccountBaseRepository<Acc extends Account> extends JpaRepository<Acc, Long> {
    Optional<Acc> findById(Long id);
}
