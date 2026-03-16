package com.example.OpenBankingApiTask.service;

import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    public BalanceResponse getBalance(String iban) {
        //send request to external bank
        return new BalanceResponse();
    }

    public List<TransactionDto> getTransactions(String iban) {
        //send request to external bank
        return new ArrayList<>();
    }
}
