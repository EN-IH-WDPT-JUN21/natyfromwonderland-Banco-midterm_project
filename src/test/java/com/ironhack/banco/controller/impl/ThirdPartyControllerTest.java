package com.ironhack.banco.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.banco.BancoApplication;
import com.ironhack.banco.dao.accounts.BusinessLogic;
import com.ironhack.banco.dao.utils.ThirdParty;
import com.ironhack.banco.repository.ThirdPartyRepository;
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

    private ThirdParty thirdParty;
    private ThirdParty thirdParty2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        thirdParty = new ThirdParty("th567483jnskt679", "Adam Smith Inc");

        thirdPartyRepository.save(thirdParty);

    }

    @AfterEach
    void tearDown(){
        thirdPartyRepository.deleteAll();
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
}