package com.ironhack.banco.dao.accounts;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.banco.dao.utils.AccountHolder;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
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

    @JsonManagedReference
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions = new ArrayList<>();


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

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }


    public Account(Long id, Money balance, Long secretKey, Date creationDate, AccountHolder primaryOwner, List<Transaction> transactions) {
        this.id = id;
        this.balance = balance;
        this.secretKey = secretKey;
        this.creationDate = creationDate;
        this.primaryOwner = primaryOwner;
        setTransactions(transactions);
    }

    //This will be needed for account creation process
    public Long checkPrimaryOwnerAge(Date date){
        Long difference = date.getTime() - this.getPrimaryOwner().getDateOfBirth().getTime();
        Long age = (difference / (1000l*60*60*24*365));
        return age;
    }
}
