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
public class Savings extends Account{

    @DecimalMax("0.5")
    private BigDecimal interestRate = new BigDecimal("0.0025");

    @Embedded
    private Money minBalance = new Money(new BigDecimal("1000.00"));

    public void applyInterest(LocalDate date){
        Period diff = Period.between(getCreationDate(), date);
        int years = diff.getYears();
        if (years>=1) {
            BigDecimal appliedInterest = BigDecimal.ONE.add(interestRate).pow(years).multiply(getBalance().getAmount());
            setBalance(new Money(appliedInterest));
            if (getBalance().getAmount().doubleValue() < minBalance.getAmount().doubleValue()) {
                getBalance().decreaseAmount(getPenaltyFee().getAmount());
            }
        }
    }

    public void setMinBalance(Money minBalance){
        if(minBalance.getAmount().doubleValue()<100.00){
            minBalance.setAmount(new BigDecimal("100.00"));
        }
        this.minBalance = minBalance;
    }

    public Savings(Long id, Money balance, Long secretKey, LocalDate creationDate, AccountHolder primaryOwner,
                   List<Transaction> transactions, BigDecimal interestRate, Money minBalance) {
        super(id, balance, secretKey, creationDate, primaryOwner, transactions);
        this.interestRate = interestRate;
        setMinBalance(minBalance);
    }
}
