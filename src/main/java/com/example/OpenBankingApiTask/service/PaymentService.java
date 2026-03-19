package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.ExternalPaymentResponse;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.entity.Payment;
import com.example.OpenBankingApiTask.enums.PaymentStatus;
import com.example.OpenBankingApiTask.exception.CurrencyMismatchException;
import com.example.OpenBankingApiTask.exception.InsufficientFundsException;
import com.example.OpenBankingApiTask.exception.PaymentNotFoundException;
import com.example.OpenBankingApiTask.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void validate(PaymentRequest request, BalanceResponse balance) {
        if (!balance.getCurrency().equals(request.getCurrency())) {
            throw new CurrencyMismatchException("Currency mismatch");
        }
        if (balance.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

    @Transactional
    public Payment createPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setFromIban(request.getFromIban());
        payment.setToIban(request.getToIban());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus(PaymentStatus.CREATED);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment updateAfterExternal(Long id, ExternalPaymentResponse response) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id " + id + " not found"));
        payment.setExternalPaymentId(response.getPaymentId());
        payment.setStatus(PaymentStatus.INITIATED);
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }
}
