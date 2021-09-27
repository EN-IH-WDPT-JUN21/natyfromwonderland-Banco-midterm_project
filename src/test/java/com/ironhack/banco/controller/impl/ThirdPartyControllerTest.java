package com.ironhack.banco.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.banco.BancoApplication;
import com.ironhack.banco.dao.accounts.Account;
import com.ironhack.banco.dao.accounts.BusinessLogic;
import com.ironhack.banco.dao.accounts.Savings;
import com.ironhack.banco.dao.accounts.Transaction;
import com.ironhack.banco.dao.utils.AccountHolder;
import com.ironhack.banco.dao.utils.Address;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.dao.utils.ThirdParty;
import com.ironhack.banco.dto.TransactionDTO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ThirdPartyControllerTest {
    @MockBean
    private BancoApplication bancoApplication;

    @Autowired
    private BusinessLogic businessLogic;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private ThirdParty thirdParty;
    private ThirdParty thirdParty2;
    private Account savings1;
    AccountHolder accountHolder;
    List<Transaction> transactions = new ArrayList<>();
    Address address;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        address = new Address(1, "Abbey Road", "NW1 3WA", "London", "United Kingdom");
        accountHolder = new AccountHolder("Adam Smith", new Date(1986,5,15), address);
        accountHolderRepository.save(accountHolder);
        thirdParty = new ThirdParty("th567483jnskt679", "Debenhams Ltd");

        thirdPartyRepository.save(thirdParty);

        savings1 = new Savings(234578784L, new Money(new BigDecimal("1000")), 563478L,
                new Date(2020,4,20), accountHolder,
                new BigDecimal("0.0025"), new Money(new BigDecimal("500")));

        accountRepository.save(savings1);

    }

    @AfterEach
    void tearDown(){
        thirdPartyRepository.deleteAll();
        savingsRepository.deleteAll();
        transactionRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void addParty_Positive() throws Exception{
            thirdParty2 = new ThirdParty("th567483jnmlkt680", "Pizza Hut");
            String body = objectMapper.writeValueAsString(thirdParty2);
            MvcResult result = mockMvc.perform(
                    post("/thirdparty")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated()).andReturn();
            assertTrue(result.getResponse().getContentAsString().contains("Pizza Hut"));

    }

    @Test
    void sendMoney_NoError() throws Exception {
        TransactionDTO transaction = new TransactionDTO(thirdParty.getHashedKey(), new Money(new BigDecimal("30")), savings1.getId(), savings1.getSecretKey());
        String body = objectMapper.writeValueAsString(transaction);
        MvcResult result = mockMvc.perform(
                post("/thirdparty/sendmoney")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertEquals(new Money(new BigDecimal("1030.00")), savings1.getBalance().getAmount());
    }
}