package com.bank.loan.disbursement.messaging;

import com.bank.loan.common.events.LoanApprovedEvent;
import com.bank.loan.common.events.LoanDisbursedEvent;
import com.bank.loan.common.messaging.LoanTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class DisbursementListener {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public DisbursementListener(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = LoanTopics.LOAN_APPROVED, groupId = "disbursement-service")
    public void disburse(String message) throws JsonProcessingException {
        LoanApprovedEvent input = objectMapper.readValue(message, LoanApprovedEvent.class);
        LoanDisbursedEvent event = new LoanDisbursedEvent(UUID.randomUUID(), input.applicationId(), input.customerId(),
                "CBS-" + input.customerId(), input.approvedAmount(), "NEFT-" + UUID.randomUUID(), Instant.now());
        kafkaTemplate.send(LoanTopics.LOAN_DISBURSED, input.applicationId(), objectMapper.writeValueAsString(event));
    }
}
