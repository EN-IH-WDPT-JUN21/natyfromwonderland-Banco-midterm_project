package com.ironhack.banco.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.ironhack.banco.BancoApplication;
import com.ironhack.banco.dao.*;
import com.ironhack.banco.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    AccountHolder accountHolder3;
    List<Transaction> transactions = new ArrayList<>();
    List<Transaction> transactions2 = new ArrayList<>();
    Address address;
    Address address2;
    Checking account;
    Checking account5;
    StudentChecking account2;
    Savings account3;
    CreditCard account4;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        address = new Address(1, "Abbey Road", "NW1 3WA", "London", "United Kingdom");
        address2 = new Address(2, "Camden New Road", "SW1 3WA", "London", "United Kingdom");
        accountHolder = new AccountHolder("Adam Smith", java.sql.Date.valueOf("1984-03-02"), address);
        accountHolder2 = new AccountHolder("Jane Ayre", java.sql.Date.valueOf("1975-08-12"), address2);
        accountHolder3 = new AccountHolder("Marie Curie", java.sql.Date.valueOf("2000-09-12"), address2);
        accountHolderRepository.saveAll(List.of(accountHolder, accountHolder2, accountHolder3));
        account = new Checking(234578784L, new Money(new BigDecimal("1000")), 567478L,
                java.sql.Date.valueOf("2020-04-02"), accountHolder, transactions,new Money(new BigDecimal("150")));
        checkingRepository.save(account);
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
    void createNewChecking() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2021-09-02");
        account5 = new Checking(214578774L, new Money(new BigDecimal("2000")), 569378L,
                date, accountHolder2, transactions2, new Money(new BigDecimal("300")));
        String body = objectMapper.writeValueAsString(account5);
        MvcResult result = mockMvc.perform(
                post("/accounts/create/checking")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("214578774L"));
    }

    @Test
    void createStudentChecking() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2021-09-22");
        account2 = new StudentChecking(214578894L, new Money(new BigDecimal("1100")), 469978L,
                date, accountHolder2, transactions2);
        String body = objectMapper.writeValueAsString(account2);
        MvcResult result = mockMvc.perform(
                post("/accounts/create/studentchecking")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("214578894L"));
    }

    @Test
    void createNewCreditCard() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2021-09-22");
        account4 = new CreditCard(214578895L, new Money(new BigDecimal("1100")), 469979L,
                date, accountHolder2, transactions2,new BigDecimal("0.15"), new Money(new BigDecimal("500")));
        String body = objectMapper.writeValueAsString(account4);
        MvcResult result = mockMvc.perform(
                post("/accounts/create/creditcard")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("214578895L"));
    }

    @Test
    void createNewSavings() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2021-09-22");
        account3 = new Savings(214479895L, new Money(new BigDecimal("1100")), 369179L,
                date, accountHolder2, transactions2, new BigDecimal("0.0025"), new Money(new BigDecimal("500")));
        String body = objectMapper.writeValueAsString(account4);
        MvcResult result = mockMvc.perform(
                post("/accounts/create/savings")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("214479895L"));
    }

    @Test
    void updateBalance() throws Exception {
        Account account5 = new Account();
        account5.setBalance(new Money(new BigDecimal("1050")));
        String body = objectMapper.writeValueAsString(account5.getBalance().getAmount());
        MvcResult result = mockMvc.perform(
                patch("/accounts/" + account.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();
        assertEquals(account5.getBalance().getAmount(), accountRepository.findById(account.getId()).get().getBalance().getAmount());

    }
}