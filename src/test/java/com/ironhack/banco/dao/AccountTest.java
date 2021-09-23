package com.ironhack.banco.dao;

import com.ironhack.banco.BancoApplication;
import com.ironhack.banco.dao.accounts.Account;
import com.ironhack.banco.dao.accounts.BusinessLogic;
import com.ironhack.banco.dao.accounts.Transaction;
import com.ironhack.banco.dao.utils.AccountHolder;
import com.ironhack.banco.dao.utils.Address;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.repository.AccountHolderRepository;
import com.ironhack.banco.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountTest {

    @MockBean
    private BancoApplication bancoApplication;

    @Autowired
    private BusinessLogic businessLogic;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    Account acc1;
    Account acc2;
    AccountHolder accountHolder;
    AccountHolder accountHolder2;
    List<Transaction> transactions = new ArrayList<>();
    List<Transaction> transactions2;
    Address address;
    Address address2;

    @BeforeEach
    void setUp() {
        address = new Address(1, "Abbey Road", "NW1 3WA", "London", "United Kingdom");
        address2 = new Address(2, "Camden New Road", "SW1 3WA", "London", "United Kingdom");
        accountHolder = new AccountHolder("Adam Smith", new Date(1986,5,15), address);
        accountHolder2 = new AccountHolder("Jane Ayre", new Date(1956,7,25), address2);
        accountHolderRepository.saveAll(List.of(accountHolder, accountHolder2));
        acc1 = new Account(234578784L, new Money(new BigDecimal("1000")), 567478L,
                new Date(2021,4,20), accountHolder, transactions);
        acc2 = new Account(234578234L, new Money(new BigDecimal("350")), 567498L,
                new Date(2021,3,24), accountHolder2, transactions2);
        accountRepository.saveAll(List.of(acc1, acc2));
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void sendMoney() throws Exception {
        acc1.sendMoney(new Money(new BigDecimal("45")));
        assertEquals(new BigDecimal("955.00"), acc1.getBalance().getAmount());
    }

    @Test
    void receiveMoney() {
        acc1.receiveMoney(new Money(new BigDecimal("50")));
        assertEquals(new BigDecimal("1050.00"), acc1.getBalance().getAmount());
    }

    @Test
    void addTransaction() {
       Transaction transaction = new Transaction(new Money(new BigDecimal("60")),
               new Timestamp(2021, 9, 20, 19, 0, 0, 0), acc1);
        acc1.addTransaction(transaction);
        accountRepository.save(acc1);
        assertEquals(1, acc1.getTransactions().size());
    }

    @Test
    void checkPrimaryOwnerAge(){
        assertEquals(35, acc1.checkPrimaryOwnerAge(new Date(2021, 9, 19)));
    }
}