package com.bank.loan.common.events;

import java.time.Instant;
import java.util.UUID;

public record LoanRejectedEvent(
        UUID eventId,
        String applicationId,
        String customerId,
        String rejectionCode,
        String rejectionReason,
        Instant occurredAt
) implements LoanEvent {
    @Override
    public String eventType() {
        return "LoanRejected";
    }
}
