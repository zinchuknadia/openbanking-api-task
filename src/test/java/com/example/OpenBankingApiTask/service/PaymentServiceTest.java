package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.exception.CurrencyMismatchException;
import com.example.OpenBankingApiTask.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @Test
    void shouldThrowException_whenCurrencyMismatch() {
        PaymentRequest request = new PaymentRequest();
        request.setCurrency("USD");

        BalanceResponse balance = new BalanceResponse();
        balance.setCurrency("EUR");

        Throwable ex = assertThrows(CurrencyMismatchException.class, () ->
                paymentService.validateCurrency(request, balance)
        );
        assertEquals("Currency mismatch", ex.getMessage());
    }

    @Test
    void shouldThrowException_whenInsufficientFunds() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(BigDecimal.valueOf(1000));

        BalanceResponse balance = new BalanceResponse();
        balance.setBalance(BigDecimal.valueOf(100));

        Throwable ex = assertThrows(InsufficientFundsException.class, () ->
                paymentService.validateAmount(request, balance)
        );
        assertEquals("Insufficient funds", ex.getMessage());
    }
}