package com.example.OpenBankingApiTask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    @Schema(example = "Bad request")
    String status;

    @Schema(example = "Invalid IBAN format")
    String message;

    @Schema(example = "2026-03-20T12:00:00")
    private LocalDateTime timestamp;

    public ErrorResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
