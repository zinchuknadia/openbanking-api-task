package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.client.ExternalBankClient;
import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.ExternalPaymentResponse;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.dto.PaymentResponse;
import com.example.OpenBankingApiTask.entity.Payment;
import com.example.OpenBankingApiTask.enums.PaymentStatus;
import com.example.OpenBankingApiTask.exception.CurrencyMismatchException;
import com.example.OpenBankingApiTask.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentOrchestratorTest {
    @Mock
    private AccountService accountService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private ExternalBankClient externalBankClient;

    @InjectMocks
    private PaymentOrchestrator paymentOrchestrator;

    @Test
    void shouldInitiatePayment() {
        PaymentRequest request = new PaymentRequest();

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.INITIATED);
        payment.setExternalPaymentId("ext-123");

        when(accountService.getBalance(any()))
                .thenReturn(new BalanceResponse());

        doNothing().when(paymentService).validateCurrency(any(), any());
        doNothing().when(paymentService).validateAmount(any(), any());

        when(paymentService.createPayment(any()))
                .thenReturn(payment);

        when(externalBankClient.sendPayment(any()))
                .thenReturn(new ExternalPaymentResponse());

        when(paymentService.updateAfterExternal(eq(1L), any()))
                .thenReturn(payment);

        PaymentResponse response = paymentOrchestrator.initiatePayment(request);

        assertEquals(1L, response.getPaymentId());
        assertEquals(PaymentStatus.INITIATED, response.getStatus());
        assertEquals("ext-123", response.getExternalPaymentId());
    }

    @Test
    void shouldMarkPaymentAsFailed_whenExternalCallFails() {
        PaymentRequest request = new PaymentRequest();
        Payment payment = new Payment();
        payment.setId(1L);

        when(accountService.getBalance(any())).thenReturn(new BalanceResponse());
        doNothing().when(paymentService).validateCurrency(any(), any());
        doNothing().when(paymentService).validateAmount(any(), any());
        when(paymentService.createPayment(any())).thenReturn(payment);

        when(externalBankClient.sendPayment(any()))
                .thenThrow(new RuntimeException("Bank error"));

        assertThrows(RuntimeException.class, () ->
                paymentOrchestrator.initiatePayment(request)
        );

        verify(paymentService).markAsFailed(1L);
        verify(paymentService, never()).updateAfterExternal(any(), any());
    }

    @Test
    void shouldNotValidateBalanceAmountAndProceed_whenCurrencyValidationFails() {
        PaymentRequest request = new PaymentRequest();
        BalanceResponse balance = new BalanceResponse();

        when(accountService.getBalance(any())).thenReturn(balance);

        doThrow(new CurrencyMismatchException("Currency mismatch"))
                .when(paymentService)
                .validateCurrency(any(), any());

        assertThrows(CurrencyMismatchException.class, () ->
                paymentOrchestrator.initiatePayment(request)
        );

        verify(paymentService, never()).validateAmount(any(), any());
        verify(paymentService, never()).createPayment(any());
        verify(externalBankClient, never()).sendPayment(any());
        verify(paymentService, never()).markAsFailed(any());
    }

    @Test
    void shouldNotProceed_whenBalanceValidationFails() {
        PaymentRequest request = new PaymentRequest();
        BalanceResponse balance = new BalanceResponse();

        when(accountService.getBalance(any())).thenReturn(balance);
        doNothing().when(paymentService).validateCurrency(any(), any());

        doThrow(new InsufficientFundsException("Insufficient funds"))
                .when(paymentService)
                .validateAmount(any(), any());

        assertThrows(InsufficientFundsException.class, () ->
                paymentOrchestrator.initiatePayment(request)
        );

        verify(paymentService, never()).createPayment(any());
        verify(externalBankClient, never()).sendPayment(any());
        verify(paymentService, never()).markAsFailed(any());
    }
}