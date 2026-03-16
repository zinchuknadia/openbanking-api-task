package com.example.OpenBankingApiTask.controller;

import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import com.example.OpenBankingApiTask.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{iban}/balance")
    public BalanceResponse getBalance(@PathVariable String iban) {
        return accountService.getBalance(iban);
    }

    @GetMapping("/{iban}/transactions")
    public List<TransactionDto> getTransactions(@PathVariable String iban) {
        return accountService.getTransactions(iban);
    }
}
