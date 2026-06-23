package com.bank.loan.common.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LoanApprovedEvent(
        UUID eventId,
        String applicationId,
        String customerId,
        BigDecimal approvedAmount,
        BigDecimal annualInterestRate,
        String approvalAuthority,
        Instant occurredAt
) implements LoanEvent {
    @Override
    public String eventType() {
        return "LoanApproved";
    }
}
