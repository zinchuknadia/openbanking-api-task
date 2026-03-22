package com.example.OpenBankingApiTask.controller;

import com.example.OpenBankingApiTask.dto.PaymentResponse;
import com.example.OpenBankingApiTask.enums.PaymentStatus;
import com.example.OpenBankingApiTask.exception.CurrencyMismatchException;
import com.example.OpenBankingApiTask.exception.InsufficientFundsException;
import com.example.OpenBankingApiTask.exception.PaymentNotFoundException;
import com.example.OpenBankingApiTask.service.PaymentOrchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentOrchestrator paymentOrchestrator;

    @Test
    void shouldInitiatePayment() throws Exception {
        Long paymentId = 1L;
        PaymentStatus status = PaymentStatus.INITIATED;
        String externalId = "ext-123";
        PaymentResponse response = new PaymentResponse(paymentId, status, externalId);

        when(paymentOrchestrator.initiatePayment(any())).thenReturn(response);

        mockMvc.perform(post("/api/payments/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "fromIban": "UA12356789876544",
                                      "toIban": "UA45656789876544",
                                      "amount": 100,
                                      "currency": "USD"
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value(paymentId))
                .andExpect(jsonPath("$.status").value(status.toString()))
                .andExpect(jsonPath("$.externalPaymentId").value(externalId));

        verify(paymentOrchestrator).initiatePayment(any());
    }

    @Test
    void shouldReturn400_whenCurrencyMismatch() throws Exception {

        when(paymentOrchestrator.initiatePayment(any()))
                .thenThrow(new CurrencyMismatchException("Currency mismatch"));

        mockMvc.perform(post("/api/payments/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "fromIban": "UA1232345324234542",
                                      "toIban": "UA456377628438345",
                                      "amount": 100,
                                      "currency": "USD"
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Currency mismatch"))
                .andExpect(jsonPath("$.status").value("Bad Request"));
    }

    @Test
    void shouldReturn400_whenInsufficientFunds() throws Exception {

        when(paymentOrchestrator.initiatePayment(any()))
                .thenThrow(new InsufficientFundsException("Insufficient funds"));

        mockMvc.perform(post("/api/payments/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "fromIban": "UA1232345324234542",
                                      "toIban": "UA456377628438345",
                                      "amount": 100,
                                      "currency": "USD"
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Insufficient funds"))
                .andExpect(jsonPath("$.status").value("Bad Request"));
    }

    @Test
    void shouldReturn404_whenPaymentNotFound() throws Exception {

        when(paymentOrchestrator.initiatePayment(any()))
                .thenThrow(new PaymentNotFoundException("Payment not found"));

        mockMvc.perform(post("/api/payments/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "fromIban": "UA1235734334826473",
                                      "toIban": "UA4563848374265293",
                                      "amount": 100,
                                      "currency": "USD"
                                    }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Payment not found"))
                .andExpect(jsonPath("$.status").value("Not Found"));
    }

    @Test
    void shouldReturn400_whenRequestInvalid() throws Exception {

        mockMvc.perform(post("/api/payments/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "fromIban": "UA12",
                                      "toIban": "UA4563848374265293",
                                      "amount": 100,
                                      "currency": "USD"
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("fromIban: Invalid IBAN format"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.toString()));

        verify(paymentOrchestrator, never()).initiatePayment(any());
    }
}