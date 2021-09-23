package com.ironhack.banco.dao;

import com.ironhack.banco.BancoApplication;
import com.ironhack.banco.dao.accounts.BusinessLogic;
import com.ironhack.banco.dao.accounts.CreditCard;
import com.ironhack.banco.dao.accounts.Transaction;
import com.ironhack.banco.dao.utils.AccountHolder;
import com.ironhack.banco.dao.utils.Address;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreditCardTest {

    @MockBean
    private BancoApplication bancoApplication;

    @Autowired
    private BusinessLogic businessLogic;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    CreditCard cc1;
    CreditCard cc2;
    AccountHolder accountHolder;
    List<Transaction> transactions;
    Address address;

    @BeforeEach
    void setUp() {
        address = new Address(1, "Abbey Road", "NW1 4WA", "London", "United Kingdom");
        accountHolder = new AccountHolder("Jane Smith", new Date(1986,5,15), address);
        accountHolderRepository.save(accountHolder);
        cc1 = new CreditCard(234578784L, new Money(new BigDecimal("1000")), 567478L,
                new Date(2021,4,20), accountHolder, transactions,
                new BigDecimal("0.15"), new Money(new BigDecimal("500")));
        cc2 = new CreditCard(234578234L, new Money(new BigDecimal("350")), 567498L,
                new Date(2021,3,24), accountHolder, transactions,
                new BigDecimal("0.15"), new Money(new BigDecimal("101000")));
        creditCardRepository.saveAll(List.of(cc1, cc2));
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        creditCardRepository.deleteAll();
    }

    @Test
    void applyInterest() {
        cc1.applyInterest(new Date(2021, 9, 19));
        assertEquals(new BigDecimal("1064.08"), cc1.getBalance().getAmount());

    }

    @Test
    void sendMoneyCC() throws Exception {
        Money amount = new Money(new BigDecimal("35"));
        cc2.sendMoney(amount);
        assertEquals(new BigDecimal("315.00"), cc2.getBalance().getAmount());
    }

    @Test
    void setCreditLimit() {
        assertEquals(new BigDecimal("100000"), cc2.getCreditLimit().getAmount());
    }
}