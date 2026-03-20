package com.example.OpenBankingApiTask.dto;

import com.example.OpenBankingApiTask.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    @Schema(example = "1")
    private Long paymentId;

    @Schema(example = "INITIATED")
    private PaymentStatus status;

    @Schema(example = "ext-1234")
    private String externalPaymentId;
}
