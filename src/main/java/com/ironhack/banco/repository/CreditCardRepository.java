package com.ironhack.banco.repository;

import com.ironhack.banco.dao.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository <CreditCard, Long> {
}
