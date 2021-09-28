package com.ironhack.banco.dao.accounts;

import com.ironhack.banco.dao.utils.AccountHolder;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "accounts")
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

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "primary_owner_id")
    private AccountHolder primaryOwner;

    @ManyToOne
    @Valid
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;


    public void sendMoney(Money amount) throws Exception {
        Money newBalance = new Money(balance.decreaseAmount(amount));
        if(newBalance.getAmount().doubleValue() >= 0) {
            this.setBalance(newBalance);
        } else {
            throw new Exception("Insufficient funds");
        }
    }

    public void receiveMoney(Money amount){
        Money newBalance = new Money(balance.increaseAmount(amount));
        this.setBalance(newBalance);
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Account(Long id, Money balance, Long secretKey, Date creationDate, AccountHolder primaryOwner) {
        this.id = id;
        setBalance(balance);
        this.secretKey = secretKey;
        this.creationDate = creationDate;
        this.primaryOwner = primaryOwner;
    }

    //This will be needed for account creation process, method to check owner's age
    public Long checkPrimaryOwnerAge(Date date){
        Long difference = date.getTime() - this.getPrimaryOwner().getDateOfBirth().getTime();
        Long age = (difference / (1000l*60*60*24*365));
        return age;
    }
}
