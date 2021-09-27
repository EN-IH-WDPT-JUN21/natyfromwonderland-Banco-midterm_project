package com.ironhack.banco.dao.accounts;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ironhack.banco.dao.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

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

    private Date transactionTime;

    private Long senderId;
    private Long receiverId;

    public Transaction(Money transactionAmount, Date transactionTime, Long senderId, Long receiverId) {
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public Transaction(Money transactionAmount, Date transactionTime, Long senderId) {
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.senderId = senderId;
    }

}
