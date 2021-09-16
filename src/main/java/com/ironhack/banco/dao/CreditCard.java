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
    @DecimalMax("100000.00")
    private Money creditLimit = new Money(new BigDecimal("100.00"));

    public void setBalance(Money balance){
        LocalDate today = LocalDate.now();
        Period diff = Period.between(today, getCreationDate());
        int months = diff.getMonths();
        if (months>=1 && balance.getAmount().doubleValue() > 0 ) {
            BigDecimal appliedInterest = BigDecimal.ONE.add(interestRate).pow(months).multiply(balance.getAmount());
            balance.decreaseAmount(appliedInterest);
        }
        balance = balance;
    }

    
    public void sendMoney(BigDecimal amount) throws Exception {
        Money newBalance = new Money(getBalance().increaseAmount(amount));
        if(newBalance.getAmount().doubleValue() <= creditLimit.getAmount().doubleValue()) {
            this.setBalance(newBalance);
        } else {
            throw new Exception("Exceeds credit limit");
        }
    }

}
