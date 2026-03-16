package com.example.OpenBankingApiTask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String iban;
    private BigDecimal amount;
    private String currency;
    private String description;
    private LocalDate date;
}
