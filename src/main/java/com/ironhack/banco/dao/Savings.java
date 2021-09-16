package com.ironhack.banco.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Savings extends Account{

    @DecimalMin("0.5")
    private BigDecimal interestRate = new BigDecimal("0.0025");

    @Embedded
    @DecimalMin("100.00")
    private Money minBalance = new Money(new BigDecimal("1000.00"));

    public void setBalance(Money balance){
        LocalDate today = LocalDate.now();
        Period diff = Period.between(today, getCreationDate());
        int years = diff.getYears();
        if (years>=1) {
            BigDecimal appliedInterest = BigDecimal.ONE.add(interestRate).pow(years).multiply(balance.getAmount());
            balance.increaseAmount(appliedInterest);
            if (balance.getAmount().doubleValue() < minBalance.getAmount().doubleValue()) {
                balance.decreaseAmount(getPenaltyFee().getAmount());
            }
        }
        balance = balance;

    }
}
