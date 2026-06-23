package com.bank.loan.common.events;

import com.bank.loan.common.domain.Decision;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RiskAssessedEvent(
        UUID eventId,
        String applicationId,
        String customerId,
        BigDecimal probabilityOfDefault,
        BigDecimal debtToIncomeRatio,
        Decision decision,
        Instant occurredAt
) implements LoanEvent {
    @Override
    public String eventType() {
        return "RiskAssessed";
    }
}
