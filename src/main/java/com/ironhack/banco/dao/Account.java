package com.ironhack.banco.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Account {
    @Id
    private Long id;

    @AttributeOverrides({
            @AttributeOverride(name="amount",column=@Column(name="balance")),
            @AttributeOverride(name="currency",column=@Column(name="balance_currency"))
    })

    @Embedded
    private Money balance;
    private Long secretKey;

    @AttributeOverrides({
            @AttributeOverride(name="amount",column=@Column(name="penalty_fee")),
            @AttributeOverride(name="currency",column=@Column(name="penalty_currency"))
    })
    @Embedded
    private final Money penaltyFee = new Money(new BigDecimal("40.00"));
    private Date creationDate;

    @Enumerated
    private Status status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    @Valid
    private AccountHolder primaryOwner;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    @Valid
    private AccountHolder secondaryOwner;

    public Optional getSecondaryOwner() {
        return Optional.ofNullable(this.secondaryOwner);
    }

    public void setSecondaryOwner(final AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }


}
