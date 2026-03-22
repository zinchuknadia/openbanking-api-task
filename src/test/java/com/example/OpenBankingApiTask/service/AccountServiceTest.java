package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.client.ExternalBankClient;
import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private ExternalBankClient externalBankClient;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldReturnBalance_whenGetBalanceCalled() {
        String iban = "UA124567890123456";
        BalanceResponse mockResponse = new BalanceResponse();

        when(externalBankClient.getBalance(iban)).thenReturn(mockResponse);

        BalanceResponse result = accountService.getBalance(iban);

        assertEquals(mockResponse, result);
        verify(externalBankClient).getBalance(iban);
    }

    @Test
    void shouldReturnTransactions_whenGetTransactionsCalled() {
        String iban = "UA124567890123456";
        List<TransactionDto> mockTransactions = List.of(new TransactionDto());

        when(externalBankClient.getTransactions(iban)).thenReturn(mockTransactions);

        List<TransactionDto> result = accountService.getTransactions(iban);
        assertEquals(mockTransactions, result);
        verify(externalBankClient).getTransactions(iban);
    }
}