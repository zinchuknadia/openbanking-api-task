package com.example.OpenBankingApiTask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    @Schema(example = "UA124567890123456")
    private String iban;

    @Schema(example = "1000")
    private BigDecimal amount;

    @Schema(example = "EUR")
    private String currency;

    @Schema(example = "Salary")
    private String description;

    @Schema(example = "2026-03-20")
    private LocalDate date;
}
