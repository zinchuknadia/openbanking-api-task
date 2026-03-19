package com.example.OpenBankingApiTask.mock;

import com.example.OpenBankingApiTask.dto.*;
import com.example.OpenBankingApiTask.enums.PaymentStatus;
import jakarta.validation.Valid;
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
    public BalanceResponse getBalance(@PathVariable String iban) {
        return new BalanceResponse(
                iban,
                BigDecimal.valueOf(1000),
                "EUR"
        );
    }

    @GetMapping("/accounts/{iban}/transactions")
    public List<TransactionDto> getTransactions(@PathVariable String iban) {
        return List.of(
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
    }

    @PostMapping("/payments")
    public ExternalPaymentResponse savePayment(@Valid @RequestBody PaymentRequest request) {
        return new ExternalPaymentResponse("1234", PaymentStatus.ACCEPTED);
    }
}
