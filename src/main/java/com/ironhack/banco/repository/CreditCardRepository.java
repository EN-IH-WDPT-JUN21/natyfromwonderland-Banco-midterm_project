package com.ironhack.banco.repository;

import com.ironhack.banco.dao.accounts.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
