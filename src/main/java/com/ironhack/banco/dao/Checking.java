package com.ironhack.banco.dao;

import com.ironhack.banco.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

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
    @DecimalMin("250.00")
    private Money minBalance;

    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee")),
            @AttributeOverride(name = "currency", column = @Column(name = "maintenance_currency"))
    })

    @Embedded
    private final Money monthlyMaintenanceFee = new Money(new BigDecimal("12.00"));

    public void setBalance(Money balance){
        LocalDate today = LocalDate.now();
        Period diff = Period.between(today, getCreationDate());
        BigDecimal months = new BigDecimal(diff.getMonths());
        if (balance.getAmount().doubleValue() >= monthlyMaintenanceFee.getAmount().doubleValue() && months.doubleValue()>=1) {
            BigDecimal appliedFee = monthlyMaintenanceFee.getAmount().multiply(months);
            balance.decreaseAmount(appliedFee);
            if (balance.getAmount().doubleValue() < minBalance.getAmount().doubleValue()) {
                balance.decreaseAmount(getPenaltyFee().getAmount());
            }
        } else {
            setStatus(Status.FROZEN);
        }
        balance = balance;

    }

}
