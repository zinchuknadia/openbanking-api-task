package com.example.OpenBankingApiTask.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentRequest {
    private String fromIban;
    private String toIban;
    private BigDecimal amount;
    private String currency;
}
