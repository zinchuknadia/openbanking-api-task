package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.client.ExternalBankClient;
import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final ExternalBankClient externalBankClient;

    public AccountService(ExternalBankClient externalBankClient) {
        this.externalBankClient = externalBankClient;
    }

    public BalanceResponse getBalance(String iban) {
        return externalBankClient.getBalance(iban);
    }

    public List<TransactionDto> getTransactions(String iban) {
        return externalBankClient.getTransactions(iban);
    }
}
