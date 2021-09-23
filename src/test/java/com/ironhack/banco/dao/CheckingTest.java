package com.ironhack.banco.dao;

import com.ironhack.banco.BancoApplication;
import com.ironhack.banco.dao.accounts.BusinessLogic;
import com.ironhack.banco.dao.accounts.Checking;
import com.ironhack.banco.dao.accounts.Transaction;
import com.ironhack.banco.dao.utils.AccountHolder;
import com.ironhack.banco.dao.utils.Address;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.repository.AccountHolderRepository;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.repository.CheckingRepository;
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
class CheckingTest {

    @MockBean
    private BancoApplication bancoApplication;

    @Autowired
    private BusinessLogic businessLogic;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    Checking ch1;
    Checking ch2;
    AccountHolder accountHolder;
    List<Transaction> transactions;
    Address address;

    @BeforeEach
    void setUp() {
        address = new Address(1, "Abbey Road", "NW1 3WA", "London", "United Kingdom");
        accountHolder = new AccountHolder("Adam Smith", new Date(1986,5,15), address);
        accountHolderRepository.save(accountHolder);
        ch1 = new Checking(234578784L, new Money(new BigDecimal("1000")), 567478L,
                new Date(2021,4,20), accountHolder, transactions, new Money(new BigDecimal("150")));
        ch2 = new Checking(234578234L, new Money(new BigDecimal("350")), 567498L,
                new Date(2021,3,24), accountHolder, transactions, new Money(new BigDecimal("300")));
        checkingRepository.saveAll(List.of(ch1, ch2));

    }

    @AfterEach
    void tearDown() {
        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void setMinBalance() {
        assertEquals(new BigDecimal("250.00"), ch1.getMinBalance().getAmount());
    }

    @Test
    void applyFees() {
        ch1.applyFees(new Date(2021, 9, 19));
        assertEquals(new BigDecimal("940.00"), ch1.getBalance().getAmount());
    }
}