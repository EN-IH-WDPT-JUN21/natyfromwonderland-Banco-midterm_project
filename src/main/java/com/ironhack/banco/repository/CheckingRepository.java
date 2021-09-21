package com.ironhack.banco.repository;

import com.ironhack.banco.dao.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
public interface CheckingRepository extends AccountBaseRepository<Checking> {
}
