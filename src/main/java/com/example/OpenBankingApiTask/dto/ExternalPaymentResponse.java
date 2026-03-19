package com.example.OpenBankingApiTask.dto;

import com.example.OpenBankingApiTask.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalPaymentResponse {
    private String paymentId;
    private PaymentStatus status;
}
