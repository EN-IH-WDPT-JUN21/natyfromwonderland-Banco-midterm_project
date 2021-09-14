package com.ironhack.banco.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Checking extends Account{

    @AttributeOverrides({
            @AttributeOverride(name="amount",column=@Column(name="min_balance")),
            @AttributeOverride(name="currency",column=@Column(name="minbalance_currency"))
    })

    @Embedded
    @DecimalMin("250.00")
    private Money minBalance;

    @AttributeOverrides({
            @AttributeOverride(name="amount",column=@Column(name="monthly_maintenance_fee")),
            @AttributeOverride(name="currency",column=@Column(name="maintenance_currency"))
    })

    @Embedded
    private final Money monthlyMaintenanceFee = new Money(new BigDecimal("12.00"));
}
