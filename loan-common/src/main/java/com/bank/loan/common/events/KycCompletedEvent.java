package com.bank.loan.common.events;

import com.bank.loan.common.domain.Decision;

import java.time.Instant;
import java.util.UUID;

public record KycCompletedEvent(
        UUID eventId,
        String applicationId,
        String customerId,
        Decision amlDecision,
        Decision sanctionsDecision,
        String riskBand,
        Instant occurredAt
) implements LoanEvent {
    @Override
    public String eventType() {
        return "KycCompleted";
    }
}
