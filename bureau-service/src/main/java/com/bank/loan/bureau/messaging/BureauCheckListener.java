package com.bank.loan.bureau.messaging;

import com.bank.loan.common.domain.Decision;
import com.bank.loan.common.events.BureauCheckCompletedEvent;
import com.bank.loan.common.events.KycCompletedEvent;
import com.bank.loan.common.events.LoanRejectedEvent;
import com.bank.loan.common.messaging.LoanTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class BureauCheckListener {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public BureauCheckListener(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = LoanTopics.KYC_COMPLETED, groupId = "bureau-service")
    public void check(String message) throws JsonProcessingException {
        KycCompletedEvent input = objectMapper.readValue(message, KycCompletedEvent.class);
        int score = Math.abs(input.customerId().hashCode() % 300) + 550;
        if (score < 620) {
            publish(LoanTopics.LOAN_REJECTED, input.applicationId(), new LoanRejectedEvent(UUID.randomUUID(),
                    input.applicationId(), input.customerId(), "LOW_BUREAU_SCORE", "Credit score below policy floor", Instant.now()));
            return;
        }
        publish(LoanTopics.BUREAU_COMPLETED, input.applicationId(), new BureauCheckCompletedEvent(UUID.randomUUID(),
                input.applicationId(), input.customerId(), score, Math.max(1, score % 8), Decision.PASS, Instant.now()));
    }

    private void publish(String topic, String key, Object event) throws JsonProcessingException {
        kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(event));
    }
}
