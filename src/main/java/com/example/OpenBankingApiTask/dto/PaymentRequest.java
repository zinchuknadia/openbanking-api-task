package com.example.OpenBankingApiTask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentRequest {
    @NotBlank(message = "Sender IBAN must not be empty")
    @Schema(example = "UA123456789012345")
    @Pattern(
            regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{10,30}$",
            message = "Invalid IBAN format"
    )
    private String fromIban;
    @NotBlank(message = "Receiver IBAN must not be empty")
    @Schema(example = "UA124567890123456")
    @Pattern(
            regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{10,30}$",
            message = "Invalid IBAN format"
    )
    private String toIban;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;
    @NotBlank(message = "Currency must not be empty")
    private String currency;
}
