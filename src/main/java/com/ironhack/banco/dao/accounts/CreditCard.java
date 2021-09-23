package com.ironhack.banco.dao.accounts;

import com.ironhack.banco.dao.utils.AccountHolder;
import com.ironhack.banco.dao.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends Account {

    @DecimalMin("0.1")
    private BigDecimal interestRate = new BigDecimal("0.2");

    @Embedded
    private Money creditLimit = new Money(new BigDecimal("100.00"));

    public Money applyInterest(Date date){
        BigDecimal interestApplied = interestRate.divide(new BigDecimal("12"));
        Long difference = date.getTime() - getCreationDate().getTime();
        BigDecimal months = BigDecimal.valueOf((difference / (1000l*60*60*24*30)));
        if (months.doubleValue()>=1 && getBalance().getAmount().doubleValue() > 0 ) {
            BigDecimal appliedInterest = BigDecimal.ONE.add(interestApplied).pow(months.intValue()).multiply(getBalance().getAmount());
            setBalance(new Money(appliedInterest));
        }
        return this.getBalance();
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

    public CreditCard(Long id, Money balance, Long secretKey, Date creationDate, AccountHolder primaryOwner,
                      List<Transaction> transactions, BigDecimal interestRate, Money creditLimit) {
        super(id, balance, secretKey, creationDate, primaryOwner, transactions);
        this.interestRate = interestRate;
        setCreditLimit(creditLimit);
    }
}
