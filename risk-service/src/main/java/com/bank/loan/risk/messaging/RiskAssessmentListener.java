package com.bank.loan.risk.messaging;

import com.bank.loan.common.domain.Decision;
import com.bank.loan.common.events.BureauCheckCompletedEvent;
import com.bank.loan.common.events.LoanRejectedEvent;
import com.bank.loan.common.events.RiskAssessedEvent;
import com.bank.loan.common.messaging.LoanTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

@Component
public class RiskAssessmentListener {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public RiskAssessmentListener(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = LoanTopics.BUREAU_COMPLETED, groupId = "risk-service")
    public void assess(String message) throws JsonProcessingException {
        BureauCheckCompletedEvent input = objectMapper.readValue(message, BureauCheckCompletedEvent.class);
        BigDecimal pd = BigDecimal.valueOf(Math.max(1, 850 - input.creditScore()))
                .divide(BigDecimal.valueOf(10000), 4, RoundingMode.HALF_UP);
        BigDecimal dti = BigDecimal.valueOf(input.activeTradelines()).divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP);
        if (pd.compareTo(BigDecimal.valueOf(0.025)) > 0) {
            publish(LoanTopics.LOAN_REJECTED, input.applicationId(), new LoanRejectedEvent(UUID.randomUUID(),
                    input.applicationId(), input.customerId(), "RISK_POLICY_DECLINE", "Probability of default exceeds policy threshold", Instant.now()));
            return;
        }
        publish(LoanTopics.RISK_ASSESSED, input.applicationId(), new RiskAssessedEvent(UUID.randomUUID(),
                input.applicationId(), input.customerId(), pd, dti, Decision.PASS, Instant.now()));
    }

    private void publish(String topic, String key, Object event) throws JsonProcessingException {
        kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(event));
    }
}
