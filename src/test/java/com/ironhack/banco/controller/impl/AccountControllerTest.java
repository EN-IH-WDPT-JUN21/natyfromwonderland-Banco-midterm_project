package com.ironhack.banco.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.banco.BancoApplication;
import com.ironhack.banco.dao.*;
import com.ironhack.banco.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
class AccountControllerTest {

    @MockBean
    private BancoApplication bancoApplication;

    @Autowired
    private BusinessLogic businessLogic;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    AccountHolder accountHolder;
    AccountHolder accountHolder2;
    List<Transaction> transactions = new ArrayList<>();
    List<Transaction> transactions2 = new ArrayList<>();
    Address address;
    Address address2;
    Account account;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        address = new Address(1, "Abbey Road", "NW1 3WA", "London", "United Kingdom");
        address2 = new Address(2, "Camden New Road", "SW1 3WA", "London", "United Kingdom");
        accountHolder = new AccountHolder("Adam Smith", LocalDate.of(1986,5,15), address);
        accountHolder2 = new AccountHolder("Jane Ayre", LocalDate.of(1956,7,25), address2);
        accountHolderRepository.saveAll(List.of(accountHolder, accountHolder2));
        account = new Account(234578784L, new Money(new BigDecimal("1000")), 567478L,
                LocalDate.of(2021,4,20), accountHolder, transactions);
        accountRepository.save(account);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void index() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/index")).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Welcome to Banco!"));

    }

    @Test
    void createNewChecking() {
    }

    @Test
    void createStudentChecking() {
    }

    @Test
    void createNewCreditCard() {
    }

    @Test
    void createNewSavings() {
    }

    @Test
    void updateBalance() {
    }
}