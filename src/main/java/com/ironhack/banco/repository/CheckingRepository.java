package com.ironhack.banco.repository;

import com.ironhack.banco.dao.accounts.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Long> {
}
