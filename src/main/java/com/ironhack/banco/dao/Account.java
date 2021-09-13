package com.ironhack.banco.dao;

import com.ironhack.banco.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {
    @Id
    protected Long id;

    protected Money balance;
    protected Long secretKey;
    protected final Money penaltyFee = new Money(new BigDecimal("40.00"));
    protected Date creationDate;

    @Enumerated
    protected Status status;

    @Valid
    private AccountHolder primaryOwner;

    @Valid
    private Optional<AccountHolder> secondaryOwner;
}
