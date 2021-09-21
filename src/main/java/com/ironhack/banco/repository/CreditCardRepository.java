package com.ironhack.banco.repository;

import com.ironhack.banco.dao.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
public interface CreditCardRepository extends AccountBaseRepository<CreditCard> {
}
