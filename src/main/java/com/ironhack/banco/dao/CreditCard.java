package com.ironhack.banco.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends Account{

    @DecimalMin("0.1")
    private BigDecimal interestRate = new BigDecimal("0.2");

    @Embedded
    private Money creditLimit = new Money(new BigDecimal("100.00"));

    public void applyInterest(LocalDate date){
        BigDecimal interestApplied = interestRate.divide(new BigDecimal("12"));
        Period diff = Period.between(getCreationDate(), date);
        int months = diff.getMonths();
        if (months>=1 && getBalance().getAmount().doubleValue() > 0 ) {
            BigDecimal appliedInterest = BigDecimal.ONE.add(interestApplied).pow(months).multiply(getBalance().getAmount());
            setBalance(new Money(appliedInterest));
        }
    }


    public void sendMoney(BigDecimal amount) throws Exception {
        Money newBalance = new Money(getBalance().increaseAmount(amount));
        if(newBalance.getAmount().doubleValue() <= creditLimit.getAmount().doubleValue()) {
            this.setBalance(newBalance);
        } else {
            throw new Exception("Exceeds credit limit");
        }
    }

    public void setCreditLimit( Money creditLimit){
        if(creditLimit.getAmount().doubleValue()>100000){
            creditLimit.setAmount(new BigDecimal("100000"));
        }
        this.creditLimit = creditLimit;
    }

    public CreditCard(Long id, Money balance, Long secretKey, LocalDate creationDate, AccountHolder primaryOwner,
                      List<Transaction> transactions, BigDecimal interestRate, Money creditLimit) {
        super(id, balance, secretKey, creationDate, primaryOwner, transactions);
        this.interestRate = interestRate;
        setCreditLimit(creditLimit);
    }
}
