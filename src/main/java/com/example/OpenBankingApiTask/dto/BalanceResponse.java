package com.example.OpenBankingApiTask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponse {
    @Schema(example = "UA124567890123456")
    private String iban;

    @Schema(example = "1000")
    private BigDecimal balance;

    @Schema(example = "EUR")
    private String currency;
}
