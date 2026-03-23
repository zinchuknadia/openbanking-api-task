package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.client.ExternalBankClient;
import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final ExternalBankClient externalBankClient;

    public AccountService(ExternalBankClient externalBankClient) {
        this.externalBankClient = externalBankClient;
    }

    public BalanceResponse getBalance(String iban) {
        LOGGER.info("Getting balance for iban = {}", iban);
        return externalBankClient.getBalance(iban);
    }

    public List<TransactionDto> getTransactions(String iban) {
        LOGGER.info("Getting transactions for iban = {}", iban);
        return externalBankClient.getTransactions(iban);
    }
}
