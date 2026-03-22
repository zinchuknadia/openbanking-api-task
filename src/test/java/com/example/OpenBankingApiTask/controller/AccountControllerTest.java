package com.example.OpenBankingApiTask.controller;

import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import com.example.OpenBankingApiTask.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void shouldReturnBalance() throws Exception {
        String iban = "UA123";

        BalanceResponse response = new BalanceResponse();
        response.setBalance(BigDecimal.valueOf(1000));
        response.setCurrency("UAH");

        when(accountService.getBalance(iban)).thenReturn(response);

        mockMvc.perform(get("/api/accounts/{iban}/balance", iban))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.currency").value("UAH"));

        verify(accountService).getBalance(iban);
    }

    @Test
    void shouldReturnTransactions() throws Exception {
        String iban = "UA123";

        TransactionDto tx = new TransactionDto();
        tx.setAmount(BigDecimal.TEN);

        when(accountService.getTransactions(iban))
                .thenReturn(List.of(tx));

        mockMvc.perform(get("/api/accounts/{iban}/transactions", iban))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(10));

        verify(accountService).getTransactions(iban);
    }
}