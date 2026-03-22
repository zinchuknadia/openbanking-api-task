package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.client.ExternalBankClient;
import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.ExternalPaymentResponse;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.dto.PaymentResponse;
import com.example.OpenBankingApiTask.entity.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrchestrator {
    private final AccountService accountService;
    private final PaymentService paymentService;
    private final ExternalBankClient externalBankClient;

    public PaymentOrchestrator(AccountService accountService, PaymentService paymentService, ExternalBankClient externalBankClient) {
        this.accountService = accountService;
        this.paymentService = paymentService;
        this.externalBankClient = externalBankClient;
    }

    public PaymentResponse initiatePayment(PaymentRequest paymentRequest) {
        BalanceResponse balance = accountService.getBalance(paymentRequest.getFromIban());
        paymentService.validateCurrency(paymentRequest, balance);
        paymentService.validateAmount(paymentRequest, balance);

        Payment payment = paymentService.createPayment(paymentRequest);

        try {
            ExternalPaymentResponse response = externalBankClient.sendPayment(paymentRequest);
            payment = paymentService.updateAfterExternal(payment.getId(), response);
        } catch (Exception e) {
            paymentService.markAsFailed(payment.getId());
            throw e;
        }
        return new PaymentResponse(
                payment.getId(),
                payment.getStatus(),
                payment.getExternalPaymentId()
        );
    }
}
