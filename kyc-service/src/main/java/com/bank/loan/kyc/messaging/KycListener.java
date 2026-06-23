package com.bank.loan.kyc.messaging;

import com.bank.loan.common.domain.Decision;
import com.bank.loan.common.events.CustomerVerifiedEvent;
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
public class KycListener {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KycListener(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = LoanTopics.CUSTOMER_VERIFIED, groupId = "kyc-service")
    public void screen(String message) throws JsonProcessingException {
        CustomerVerifiedEvent input = objectMapper.readValue(message, CustomerVerifiedEvent.class);
        if (input.customerId().endsWith("SANCTION")) {
            publish(LoanTopics.LOAN_REJECTED, input.applicationId(), new LoanRejectedEvent(UUID.randomUUID(),
                    input.applicationId(), input.customerId(), "SANCTIONS_HIT", "Customer matched sanctions screening", Instant.now()));
            return;
        }
        publish(LoanTopics.KYC_COMPLETED, input.applicationId(), new KycCompletedEvent(UUID.randomUUID(),
                input.applicationId(), input.customerId(), Decision.PASS, Decision.PASS, "LOW", Instant.now()));
    }

    private void publish(String topic, String key, Object event) throws JsonProcessingException {
        kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(event));
    }
}
