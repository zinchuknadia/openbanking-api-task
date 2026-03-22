package com.example.OpenBankingApiTask.client;

import com.example.OpenBankingApiTask.dto.BalanceResponse;
import com.example.OpenBankingApiTask.dto.ExternalPaymentResponse;
import com.example.OpenBankingApiTask.dto.PaymentRequest;
import com.example.OpenBankingApiTask.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class ExternalBankClientTest {

    @Autowired
    private ExternalBankClient client;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnBalance() {
        mockServer.expect(requestTo(containsString("/mock/bank/accounts/UA123/balance")))
                .andRespond(withSuccess("""
                            {
                              "iban": "UA123",
                              "balance": 1000,
                              "currency": "USD"
                            }
                        """, MediaType.APPLICATION_JSON));

        BalanceResponse response = client.getBalance("UA123");

        assertEquals("UA123", response.getIban());
        assertEquals("USD", response.getCurrency());
        assertEquals(1000, response.getBalance().intValue());
    }

    @Test
    void shouldReturnTransactions() {
        mockServer.expect(requestTo(containsString("/mock/bank/accounts/UA123/transactions")))
                .andRespond(withSuccess("""
                            [
                                {
                                  "iban": "UA123",
                                  "amount": 1000,
                                  "currency": "USD",
                                  "description": "Salary",
                                  "date": "2026-03-20"
                                }
                            ]
                        """, MediaType.APPLICATION_JSON));

        TransactionDto response = client.getTransactions("UA123").get(0);

        assertEquals("UA123", response.getIban());
        assertEquals("USD", response.getCurrency());
        assertEquals(1000, response.getAmount().intValue());
        assertEquals("Salary", response.getDescription());
        assertEquals(LocalDate.of(2026, 3, 20), response.getDate());
    }

    @Test
    void shouldSendPayment() {
        PaymentRequest request = new PaymentRequest();
        request.setFromIban("UA123");
        request.setToIban("UA456");
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("USD");

        mockServer.expect(requestTo(containsString("/mock/bank/payments")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("""
                            {
                              "fromIban": "UA123",
                              "toIban": "UA456",
                              "amount": 100,
                              "currency": "USD"
                            }
                        """))
                .andRespond(withSuccess("""
                            {
                              "paymentId": "ext-123",
                              "status": "CREATED"
                            }
                        """, MediaType.APPLICATION_JSON));

        ExternalPaymentResponse response = client.sendPayment(request);

        assertEquals("ext-123", response.getPaymentId());
        assertEquals("CREATED", response.getStatus().toString());
    }
}