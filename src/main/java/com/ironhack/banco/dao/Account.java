package com.ironhack.banco.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ironhack.banco.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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
    private LocalDate creationDate;

    @Enumerated
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

    @ManyToMany
    @JoinTable(
            name = "accounts_have_transactions",
            joinColumns = {@JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "transaction_id")}
    )
    private List<Transaction> transactions;

    public void freezeAcc(Account account){
        account.setStatus(Status.FROZEN);
    }

    //@Transactional
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

    public void addTransaction(Transaction transaction){
        this.transactions.add(transaction);
    }

    public BigDecimal findMaxAmount(List<Transaction> transactions){
        BigDecimal maxValue = new BigDecimal("0");
        for(int i= 0; i< transactions.size(); i++){
            if(transactions.get(i).getTransactionAmount().getAmount().doubleValue()>maxValue.doubleValue()){
                maxValue = transactions.get(i).getTransactionAmount().getAmount();
            }
        }
        return maxValue;
    }


}
