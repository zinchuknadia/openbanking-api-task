package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.client.ExternalBankClient;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.dto.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final ExternalBankClient externalBankClient;

    public PaymentService(ExternalBankClient externalBankClient) {
        this.externalBankClient = externalBankClient;
    }

    public List<PaymentResponse> initiatePayment(PaymentRequest paymentRequest) {
        //check balance
        //create payment
        //send payment to external bank
        return externalBankClient.sendPayment(paymentRequest);
    }
}
