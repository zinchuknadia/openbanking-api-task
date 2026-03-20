package com.example.OpenBankingApiTask.controller;

import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import com.example.OpenBankingApiTask.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Accounts", description = "Operations related to accounts")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Get balance by ID")
    @GetMapping("/{iban}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@Parameter(description = "Account ID", example = "UK1234567890345") @PathVariable String iban) {
        return new ResponseEntity<>(accountService.getBalance(iban), HttpStatus.OK);
    }

    @Operation(summary = "Get transactions by ID")
    @GetMapping("/{iban}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(@Parameter(description = "Account ID", example = "UK1234567890345") @PathVariable String iban) {
        return new ResponseEntity<>(accountService.getTransactions(iban), HttpStatus.OK);
    }
}
