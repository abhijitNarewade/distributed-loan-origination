package com.bank.loan.common.events;

import com.bank.loan.common.domain.LoanProduct;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ApplicationSubmittedEvent(
        UUID eventId,
        String applicationId,
        String customerId,
        LoanProduct product,
        BigDecimal requestedAmount,
        int tenureMonths,
        BigDecimal declaredMonthlyIncome,
        Instant occurredAt
) implements LoanEvent {
    @Override
    public String eventType() {
        return "ApplicationSubmitted";
    }
}
