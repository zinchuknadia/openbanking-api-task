package com.example.OpenBankingApiTask.client;

import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.dto.PaymentResponse;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ExternalBankClient {
    RestTemplate restTemplate;

    public ExternalBankClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BalanceResponse getBalance(String iban) {
        return restTemplate.getForObject(
                "/mock/bank/accounts/" + iban + "/balance",
                BalanceResponse.class);
    }

    public List<TransactionDto> getTransactions(String iban) {
        return restTemplate.getForObject(
                "/mock/bank/accounts/" + iban + "/transactions",
                List.class
        );
    }

    public List<PaymentResponse> sendPayment(PaymentRequest paymentRequest) {
        return restTemplate.postForObject(
                "/mock/bank/payments",
                paymentRequest,
                List.class
        );
    }
}