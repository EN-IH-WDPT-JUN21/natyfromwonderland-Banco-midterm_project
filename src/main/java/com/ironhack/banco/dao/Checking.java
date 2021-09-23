package com.ironhack.banco.dao;

import com.ironhack.banco.enums.Status;
import com.ironhack.banco.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Checking extends Account {

    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "min_balance")),
            @AttributeOverride(name = "currency", column = @Column(name = "minbalance_currency"))
    })

    @Embedded
    private Money minBalance;

    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee")),
            @AttributeOverride(name = "currency", column = @Column(name = "maintenance_currency"))
    })

    @Embedded
    private final Money monthlyMaintenanceFee = new Money(new BigDecimal("12.00"));

    public void setMinBalance(Money minBalance){
        if(minBalance.getAmount().doubleValue()<250.00){
            minBalance.setAmount(new BigDecimal("250.00"));
        }
        this.minBalance = minBalance;
    }

    public Money applyFees(Date date){
        Long difference = date.getTime() - getCreationDate().getTime();
        BigDecimal months = BigDecimal.valueOf((difference / (1000l*60*60*24*30)));
        if (getBalance().getAmount().doubleValue() >= monthlyMaintenanceFee.getAmount().doubleValue()
                && months.doubleValue()>=1) {
            BigDecimal appliedFee = monthlyMaintenanceFee.getAmount().multiply(months);
            setBalance(new Money(getBalance().decreaseAmount(appliedFee)));
            if (getBalance().getAmount().doubleValue() < this.minBalance.getAmount().doubleValue()
                    && getBalance().getAmount().doubleValue() > getPenaltyFee().getAmount().doubleValue()) {
                setBalance(new Money(getBalance().decreaseAmount(getPenaltyFee().getAmount())));
            }
        }
        return this.getBalance();
    }

    public Checking(Long id, Money balance, Long secretKey, Date creationDate, @Valid AccountHolder primaryOwner, List<Transaction> transactions, Money minBalance) {
        super(id, balance, secretKey, creationDate, primaryOwner, transactions);
        setMinBalance(minBalance);
    }

}
