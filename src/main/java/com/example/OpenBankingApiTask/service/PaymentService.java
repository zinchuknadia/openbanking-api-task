package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public PaymentResponse initiatePayment(PaymentRequest paymentRequest) {
        //check balance
        //create payment
        //send payment to external bank
        return new PaymentResponse();
    }
}
