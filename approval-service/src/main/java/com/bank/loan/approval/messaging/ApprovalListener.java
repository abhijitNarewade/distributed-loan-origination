package com.bank.loan.approval.messaging;

import com.bank.loan.approval.domain.ApprovalDecision;
import com.bank.loan.approval.repository.ApprovalDecisionRepository;
import com.bank.loan.common.events.LoanApprovedEvent;
import com.bank.loan.common.events.RiskAssessedEvent;
import com.bank.loan.common.messaging.LoanTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Component
public class ApprovalListener {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ApprovalDecisionRepository repository;

    public ApprovalListener(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate, ApprovalDecisionRepository repository) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
    }

    @KafkaListener(topics = LoanTopics.RISK_ASSESSED, groupId = "approval-service")
    public void approve(String message) throws JsonProcessingException {
        RiskAssessedEvent input = objectMapper.readValue(message, RiskAssessedEvent.class);
        BigDecimal approvedAmount = BigDecimal.valueOf(750000);
        BigDecimal apr = input.probabilityOfDefault().multiply(BigDecimal.valueOf(100)).add(BigDecimal.valueOf(8.75));
        String authority = input.probabilityOfDefault().compareTo(BigDecimal.valueOf(0.01)) <= 0 ? "AUTO_UNDERWRITING" : "CREDIT_MANAGER";
        LoanApprovedEvent event = new LoanApprovedEvent(UUID.randomUUID(), input.applicationId(), input.customerId(),
                approvedAmount, apr, authority, Instant.now());

        ApprovalDecision decision = new ApprovalDecision();
        decision.setApplicationId(input.applicationId());
        decision.setCustomerId(input.customerId());
        decision.setDecision("APPROVED");
        decision.setApprovedAmount(approvedAmount);
        decision.setAnnualInterestRate(apr);
        decision.setAuthority(authority);
        decision.setDecidedAt(event.occurredAt());
        repository.save(decision);

        kafkaTemplate.send(LoanTopics.LOAN_APPROVED, input.applicationId(), objectMapper.writeValueAsString(event));
    }
}
