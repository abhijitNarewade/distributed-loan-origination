package com.bank.loan.common.events;

import com.bank.loan.common.domain.Decision;

import java.time.Instant;
import java.util.UUID;

public record BureauCheckCompletedEvent(
        UUID eventId,
        String applicationId,
        String customerId,
        int creditScore,
        int activeTradelines,
        Decision decision,
        Instant occurredAt
) implements LoanEvent {
    @Override
    public String eventType() {
        return "BureauCheckCompleted";
    }
}
