package com.example.OpenBankingApiTask.client;

import com.example.OpenBankingApiTask.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ExternalBankClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExternalBankClient.class);
    private final RestTemplate restTemplate;

    public ExternalBankClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BalanceResponse getBalance(String iban) {
        LOGGER.info("Sending request to get balance for iban = {}", iban);
        return restTemplate.getForObject(
                "/mock/bank/accounts/" + iban + "/balance",
                BalanceResponse.class);
    }

    public List<TransactionDto> getTransactions(String iban) {
        LOGGER.info("Getting transactions for iban = {}", iban);
        return restTemplate.exchange(
                "/mock/bank/accounts/" + iban + "/transactions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionDto>>() {}
        ).getBody();
    }

    public ExternalPaymentResponse sendPayment(PaymentRequest request) {
        LOGGER.info("Sending request external bank fromIban = {}, toIban = {}, amount = {}",
                request.getFromIban(), request.getToIban(), request.getAmount());
        return restTemplate.postForObject(
                "/mock/bank/payments",
                request,
                ExternalPaymentResponse.class
        );
    }
}