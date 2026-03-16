package com.example.OpenBankingApiTask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponse {
    private String iban;
    private BigDecimal balance;
    private String currency;
}
