package com.bank.loan.common.events;

import com.bank.loan.common.domain.Decision;

import java.time.Instant;
import java.util.UUID;

public record CustomerVerifiedEvent(
        UUID eventId,
        String applicationId,
        String customerId,
        Decision decision,
        String reasonCode,
        Instant occurredAt
) implements LoanEvent {
    @Override
    public String eventType() {
        return "CustomerVerified";
    }
}
