package com.ironhack.banco.dto;

import com.ironhack.banco.dao.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Money transactionAmount;
    private Long accountId;
    private Long accountSecretKey;
    private String ownerName;
    private String hashedKey;

    public TransactionDTO(String hashedKey, Money transactionAmount, Long accountId, Long accountSecretKey) {
        this.hashedKey = hashedKey;
        this.transactionAmount = transactionAmount;
        this.accountId = accountId;
        this.accountSecretKey = accountSecretKey;
    }

    public TransactionDTO(Money transactionAmount, Long accountId, String ownerName) {
        this.transactionAmount = transactionAmount;
        this.accountId = accountId;
        this.ownerName = ownerName;
    }

}
