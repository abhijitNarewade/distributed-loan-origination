package com.bank.loan.customer.messaging;

import com.bank.loan.common.domain.Decision;
import com.bank.loan.common.events.ApplicationSubmittedEvent;
import com.bank.loan.common.events.CustomerVerifiedEvent;
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
public class CustomerVerificationListener {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CustomerVerificationListener(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = LoanTopics.APPLICATION_SUBMITTED, groupId = "customer-service")
    public void verify(String message) throws JsonProcessingException {
        ApplicationSubmittedEvent input = objectMapper.readValue(message, ApplicationSubmittedEvent.class);
        boolean knownCustomer = !input.customerId().startsWith("BLOCKED");
        if (!knownCustomer) {
            publish(LoanTopics.LOAN_REJECTED, input.applicationId(), new LoanRejectedEvent(UUID.randomUUID(),
                    input.applicationId(), input.customerId(), "CUSTOMER_BLOCKED", "Customer is blocked by relationship banking", Instant.now()));
            return;
        }
        publish(LoanTopics.CUSTOMER_VERIFIED, input.applicationId(), new CustomerVerifiedEvent(UUID.randomUUID(),
                input.applicationId(), input.customerId(), Decision.PASS, "CUSTOMER_MASTER_MATCH", Instant.now()));
    }

    private void publish(String topic, String key, Object event) throws JsonProcessingException {
        kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(event));
    }
}
