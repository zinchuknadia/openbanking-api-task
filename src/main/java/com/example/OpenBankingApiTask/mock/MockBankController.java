package com.example.OpenBankingApiTask.mock;

import com.example.OpenBankingApiTask.dto.*;
import com.example.OpenBankingApiTask.enums.PaymentStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/mock/bank")
@Validated
public class MockBankController {
    @GetMapping("/accounts/{iban}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable String iban) {
        BalanceResponse balance = new BalanceResponse(
                iban,
                BigDecimal.valueOf(1000),
                "EUR"
        );
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @GetMapping("/accounts/{iban}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(@PathVariable String iban) {
        List<TransactionDto> transactions = List.of(
                new TransactionDto(
                        iban,
                        BigDecimal.valueOf(1000),
                        "EUR",
                        "Salary",
                        LocalDate.of(2026, 1, 1)
                ),
                new TransactionDto(
                        iban,
                        BigDecimal.valueOf(3000),
                        "EUR",
                        "Salary",
                        LocalDate.of(2024, 10, 11)
                )
        );
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/payments")
    public ResponseEntity<ExternalPaymentResponse> savePayment(@Valid @RequestBody PaymentRequest request) {
        ExternalPaymentResponse response = new ExternalPaymentResponse("1234", PaymentStatus.ACCEPTED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
