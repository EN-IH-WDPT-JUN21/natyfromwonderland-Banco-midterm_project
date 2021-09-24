package com.ironhack.banco.dao.accounts;

import com.ironhack.banco.BancoApplication;
import com.ironhack.banco.dao.utils.AccountHolder;
import com.ironhack.banco.dao.utils.Address;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.enums.Status;
import com.ironhack.banco.repository.AccountHolderRepository;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BusinessLogicTest {

    @MockBean
    private BancoApplication bancoApplication;

    @Autowired
    private BusinessLogic businessLogic;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    Account acc1;
    Account acc2;
    AccountHolder accountHolder;
    AccountHolder accountHolder2;
    Address address;
    Address address2;
    Transaction transaction1;
    Transaction transaction2;

    @BeforeEach
    void setUp() {
        address = new Address(1, "Abbey Road", "NW1 3WA", "London", "United Kingdom");
        address2 = new Address(2, "Camden New Road", "SW1 3WA", "London", "United Kingdom");
        accountHolder = new AccountHolder("Adam Smith", new Date(1986,5,15), address);
        accountHolder2 = new AccountHolder("Jane Ayre", new Date(1956,7,25), address2);
        accountHolderRepository.saveAll(List.of(accountHolder, accountHolder2));
        acc1 = new Account(234578784L, new Money(new BigDecimal("16000")), 567478L,
                new Date(2021,4,20), accountHolder);
        acc2 = new Account(234578234L, new Money(new BigDecimal("350")), 567498L,
                new Date(2021,3,24), accountHolder2);
        accountRepository.saveAll(List.of(acc1, acc2));
        transaction1 = new Transaction(new Money(new BigDecimal("500")),
                new Date(2021, Calendar.SEPTEMBER, 20, 19, 0, 1), acc1);
        transaction2 = new Transaction(new Money(new BigDecimal("60")),
                new Date(2021, Calendar.APRIL, 23, 19, 0, 0), acc1);
        transactionRepository.saveAll(List.of(transaction1, transaction2));
        acc1.addTransaction(transaction1);
        acc1.addTransaction(transaction2);
        accountRepository.saveAll(List.of(acc1, acc2));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void freezeAcc() {
        businessLogic.freezeAcc(acc1);
        assertEquals(Status.FROZEN, acc1.getStatus());
    }

    @Test
    void notExceedMaxAmount() {
        Transaction transaction3 = new Transaction(new Money(new BigDecimal("400")),
                new Date(2021, Calendar.SEPTEMBER, 20, 19, 5, 0), acc1);
        Boolean result = businessLogic.notExceedMaxAmount(acc1, transaction3);
        assertEquals(true, result);

    }

    @Test
    void notExceedMaxCount() {
        Transaction transaction4 = new Transaction(new Money(new BigDecimal("400")),
                new Date(2021, Calendar.SEPTEMBER, 20, 19, 0, 2), acc1);
        Boolean result = businessLogic.notExceedMaxCount(acc1, transaction4);
        assertEquals(true, result);
    }

    @Test
    void findMaxAmount() {
        Transaction transaction5 = new Transaction(new Money(new BigDecimal("400")),
                new Date(2021, Calendar.SEPTEMBER, 20, 19, 0, 2), acc1);
        acc1.addTransaction(transaction5);
        accountRepository.save(acc1);
        BigDecimal result = businessLogic.findMaxAmount(acc1.getTransactions());
        assertEquals(new BigDecimal("500.00"), result);

    }

    @Test
    void addSecondsToTransactionDate(){
        Date date = new Date(2021, Calendar.SEPTEMBER, 20, 19, 0, 0);
        date = businessLogic.addSecondsToTransactionDate(date, 1);
        assertEquals(new Date(2021, Calendar.SEPTEMBER, 20, 19, 0, 1), date);

    }
}