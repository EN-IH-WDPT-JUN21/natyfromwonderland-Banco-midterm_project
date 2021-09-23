package com.ironhack.banco.repository;

import com.ironhack.banco.dao.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {
    Optional<ThirdParty> findById(Long aLong);
    Optional<ThirdParty> findByHashedKey(String hashedKey);
}
