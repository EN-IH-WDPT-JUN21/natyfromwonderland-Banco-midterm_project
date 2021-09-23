package com.ironhack.banco.dto;

import com.ironhack.banco.dao.Account;
import com.ironhack.banco.dao.Money;
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
    private String primaryOwnerName;
    private String secondaryOwnerName;

    public TransactionDTO(Money transactionAmount, Long accountId, Long accountSecretKey) {
        this.transactionAmount = transactionAmount;
        this.accountId = accountId;
        this.accountSecretKey = accountSecretKey;
    }

    public TransactionDTO(Money transactionAmount, Long accountId, String primaryOwnerName, String secondaryOwnerName) {
        this.transactionAmount = transactionAmount;
        this.accountId = accountId;
        this.primaryOwnerName = primaryOwnerName;
        this.secondaryOwnerName = secondaryOwnerName;
    }
}
