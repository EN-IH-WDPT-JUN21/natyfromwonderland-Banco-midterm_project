package com.ironhack.banco.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class StudentChecking extends Account{

    public StudentChecking(Long id, Money balance, Long secretKey, Date creationDate, AccountHolder primaryOwner, List<Transaction> transactions) {
        super(id, balance, secretKey, creationDate, primaryOwner, transactions);
    }
}
