package com.example.OpenBankingApiTask.controller;

import com.example.OpenBankingApiTask.dto.ErrorResponse;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.dto.PaymentResponse;
import com.example.OpenBankingApiTask.exception.CurrencyMismatchException;
import com.example.OpenBankingApiTask.exception.InsufficientFundsException;
import com.example.OpenBankingApiTask.exception.PaymentNotFoundException;
import com.example.OpenBankingApiTask.service.PaymentOrchestrator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payments", description = "Operations related to payments")
@RestController
@RequestMapping("/api/payments")
@Validated
public class PaymentController {
    private final PaymentOrchestrator paymentOrchestrator;

    public PaymentController(PaymentOrchestrator paymentOrchestrator) {
        this.paymentOrchestrator = paymentOrchestrator;
    }

    @Operation(
            summary = "Initiate payment",
            description = "Creates and sends payment to external bank"
    )
    @ApiResponse(responseCode = "201", description = "Payment successfully initiated")
    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(paymentOrchestrator.initiatePayment(paymentRequest), HttpStatus.CREATED);
    }

    @ExceptionHandler(CurrencyMismatchException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyMismatchException(CurrencyMismatchException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(InsufficientFundsException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Not Found", ex.getMessage()));
    }
}
