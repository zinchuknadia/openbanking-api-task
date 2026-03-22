package com.example.OpenBankingApiTask.integration;

import com.example.OpenBankingApiTask.dto.ExternalPaymentResponse;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.entity.Payment;
import com.example.OpenBankingApiTask.enums.PaymentStatus;
import com.example.OpenBankingApiTask.exception.PaymentNotFoundException;
import com.example.OpenBankingApiTask.repository.PaymentRepository;
import com.example.OpenBankingApiTask.service.PaymentService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PaymentServiceIntegrationTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
    }

    @Test
    void shouldCreatePayment() {
        String fromIban = "UA124567890123456";
        String toIban = "UA124567890123456";
        BigDecimal amount = BigDecimal.valueOf(1000);
        String currency = "EUR";

        PaymentRequest request = new PaymentRequest();
        request.setFromIban(fromIban);
        request.setToIban(toIban);
        request.setAmount(amount);
        request.setCurrency(currency);

        Payment payment = paymentService.createPayment(request);

        assertNotNull(payment.getId());
        assertEquals(fromIban, payment.getFromIban());
        assertEquals(toIban, payment.getToIban());
        assertEquals(amount, payment.getAmount());
        assertEquals(currency, payment.getCurrency());
        assertEquals(PaymentStatus.CREATED, payment.getStatus());
        assertNotNull(payment.getCreatedAt());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    void shouldUpdatePaymentAfterExternal() {
        String fromIban = "UA124567890123456";
        String toIban = "UA124567890123456";
        BigDecimal amount = BigDecimal.valueOf(1000);
        String currency = "EUR";

        PaymentRequest request = new PaymentRequest();
        request.setFromIban(fromIban);
        request.setToIban(toIban);
        request.setAmount(amount);
        request.setCurrency(currency);

        Payment payment = paymentService.createPayment(request);

        String externalPaymentId = "ext-1234";
        ExternalPaymentResponse response = new ExternalPaymentResponse();
        response.setPaymentId(externalPaymentId);
        response.setStatus(PaymentStatus.ACCEPTED);

        Payment updatedPayment = paymentService.updateAfterExternal(payment.getId(), response);

        assertEquals(externalPaymentId, updatedPayment.getExternalPaymentId());
        assertEquals(PaymentStatus.INITIATED, updatedPayment.getStatus());
        assertTrue(updatedPayment.getUpdatedAt().isAfter(updatedPayment.getCreatedAt()));
    }

    @Test
    void shouldMarkAsFailed() {
        String fromIban = "UA124567890123456";
        String toIban = "UA124567890123456";
        BigDecimal amount = BigDecimal.valueOf(1000);
        String currency = "EUR";

        PaymentRequest request = new PaymentRequest();
        request.setFromIban(fromIban);
        request.setToIban(toIban);
        request.setAmount(amount);
        request.setCurrency(currency);

        Payment payment = paymentService.createPayment(request);
        paymentService.markAsFailed(payment.getId());

        Payment failedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        assertEquals(PaymentStatus.FAILED, failedPayment.getStatus());
        assertTrue(failedPayment.getUpdatedAt().isAfter(failedPayment.getCreatedAt()));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingPayment() {
        String externalPaymentId = "ext-1234";
        PaymentStatus status = PaymentStatus.ACCEPTED;
        ExternalPaymentResponse response = new ExternalPaymentResponse(externalPaymentId, status);

        Long notExistingId = 999L;
        Throwable ex = assertThrows(PaymentNotFoundException.class, () ->
                paymentService.updateAfterExternal(notExistingId, response)
        );
        String errorMessage = "Payment with id " + notExistingId + " not found";
        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMarkingAsFailedNonExistingPayment() {
        Long notExistingId = 999L;
        Throwable ex = assertThrows(PaymentNotFoundException.class, () ->
                paymentService.markAsFailed(notExistingId)
        );
        String errorMessage = "Payment with id " + notExistingId + " not found";
        assertEquals(errorMessage, ex.getMessage());
    }
}