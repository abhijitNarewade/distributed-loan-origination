package com.bank.loan.common.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LoanDisbursedEvent(
        UUID eventId,
        String applicationId,
        String customerId,
        String coreBankingAccount,
        BigDecimal amount,
        String paymentReference,
        Instant occurredAt
) implements LoanEvent {
    @Override
    public String eventType() {
        return "LoanDisbursed";
    }
}
