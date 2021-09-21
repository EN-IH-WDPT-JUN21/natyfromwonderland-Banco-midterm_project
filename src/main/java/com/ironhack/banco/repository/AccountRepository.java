package com.ironhack.banco.repository;

import com.ironhack.banco.dao.Account;
import com.ironhack.banco.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface AccountRepository extends AccountBaseRepository<Account> {
}
