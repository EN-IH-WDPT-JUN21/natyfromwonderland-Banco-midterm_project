package com.ironhack.banco.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Money transactionAmount;

    @Basic
    private Timestamp transactionTime;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public Transaction(Money transactionAmount, Timestamp transactionTime, Account account) {
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.account = account;
    }

}
