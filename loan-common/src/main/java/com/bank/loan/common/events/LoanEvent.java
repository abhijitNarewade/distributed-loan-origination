package com.bank.loan.common.events;

import java.time.Instant;
import java.util.UUID;

public interface LoanEvent {
    UUID eventId();
    String applicationId();
    String customerId();
    Instant occurredAt();
    String eventType();
}
