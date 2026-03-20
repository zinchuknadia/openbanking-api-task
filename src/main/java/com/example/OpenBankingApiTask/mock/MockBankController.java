package com.example.OpenBankingApiTask.mock;

import com.example.OpenBankingApiTask.dto.*;
import com.example.OpenBankingApiTask.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "External Bank", description = "Mocked external bank operations")
@RestController
@RequestMapping("/mock/bank")
@Validated
public class MockBankController {

    @Operation(
            summary = "Get account balance",
            description = "Mocks getting balance by IBAN"
    )
    @GetMapping("/accounts/{iban}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@Parameter(description = "Account ID", example = "UK1234567890345") @PathVariable String iban) {
        BalanceResponse balance = new BalanceResponse(
                iban,
                BigDecimal.valueOf(1000),
                "EUR"
        );
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @Operation(
            summary = "Get account transactions",
            description = "Mocks getting transactions by IBAN"
    )
    @GetMapping("/accounts/{iban}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(@Parameter(description = "Account ID", example = "UK1234567890345") @PathVariable String iban) {
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

    @Operation(
            summary = "Process payment",
            description = "Mocks payment processing in external bank"
    )
    @ApiResponse(responseCode = "201", description = "Payment successfully processed")
    @PostMapping("/payments")
    public ResponseEntity<ExternalPaymentResponse> savePayment(@Valid @RequestBody PaymentRequest request) {
        ExternalPaymentResponse response = new ExternalPaymentResponse("ext-1234", PaymentStatus.ACCEPTED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
