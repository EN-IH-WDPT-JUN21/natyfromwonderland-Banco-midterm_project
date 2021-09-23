package com.ironhack.banco.dao;

import com.ironhack.banco.BancoApplication;
import com.ironhack.banco.dao.accounts.BusinessLogic;
import com.ironhack.banco.dao.accounts.Savings;
import com.ironhack.banco.dao.accounts.Transaction;
import com.ironhack.banco.dao.utils.AccountHolder;
import com.ironhack.banco.dao.utils.Address;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.repository.AccountHolderRepository;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.repository.SavingsRepository;
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
class SavingsTest {

    @MockBean
    private BancoApplication bancoApplication;

    @Autowired
    private BusinessLogic businessLogic;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    Savings sav1;
    Savings sav2;
    AccountHolder accountHolder;
    List<Transaction> transactions;
    Address address;

    @BeforeEach
    void setUp() {
        address = new Address(1, "Abbey Road", "NW1 3WA", "London", "United Kingdom");
        accountHolder = new AccountHolder("Adam Smith", new Date(1986,5,15), address);
        accountHolderRepository.save(accountHolder);
        sav1 = new Savings(234579784L, new Money(new BigDecimal("1000")), 567478L,
                new Date(2020,4,20), accountHolder, transactions,
                new BigDecimal("0.0025"), new Money(new BigDecimal("500")));
        sav2 = new Savings(233578234L, new Money(new BigDecimal("350")), 567498L,
                new Date(2021,3,24), accountHolder, transactions,
                new BigDecimal("0.0025"), new Money(new BigDecimal("50")));
        savingsRepository.saveAll(List.of(sav1, sav2));
    }

    @AfterEach
    void tearDown() {
        savingsRepository.deleteAll();
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void applyInterest() {
        sav1.applyInterest(new Date(2021, 9, 19));
        assertEquals(new BigDecimal("1002.50"), sav1.getBalance().getAmount());
    }

    @Test
    void setMinBalance() {
        assertEquals(new BigDecimal("100.00"), sav2.getMinBalance().getAmount());
    }
}