package com.bank.loan.common.events;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record EventEnvelope<T extends LoanEvent>(
        UUID messageId,
        String correlationId,
        String causationId,
        String aggregateId,
        long aggregateVersion,
        Instant publishedAt,
        Map<String, String> headers,
        T payload
) {
    public static <T extends LoanEvent> EventEnvelope<T> start(String correlationId, String aggregateId, T payload) {
        return new EventEnvelope<>(
                UUID.randomUUID(),
                correlationId,
                payload.eventId().toString(),
                aggregateId,
                1,
                Instant.now(),
                Map.of("eventType", payload.eventType()),
                payload
        );
    }
}
