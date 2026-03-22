package com.example.OpenBankingApiTask.client;

import com.example.OpenBankingApiTask.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
        return restTemplate.exchange(
                "/mock/bank/accounts/" + iban + "/transactions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionDto>>() {}
        ).getBody();
    }

    public ExternalPaymentResponse sendPayment(PaymentRequest request) {
        return restTemplate.postForObject(
                "/mock/bank/payments",
                request,
                ExternalPaymentResponse.class
        );
    }
}