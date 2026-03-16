package com.example.OpenBankingApiTask.mock;

import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.dto.PaymentResponse;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mock/bank")
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
                )
        );
    }

    @PostMapping("/payments")
    public List<PaymentResponse> savePayment(@RequestBody PaymentRequest paymentRequest) {
        List<PaymentResponse> responses = new ArrayList<>();
        responses.add(new PaymentResponse(paymentRequest.getFromIban(), "SUCCESS"));
        responses.add(new PaymentResponse(paymentRequest.getToIban(), "SUCCESS"));
        return responses;
    }
}
