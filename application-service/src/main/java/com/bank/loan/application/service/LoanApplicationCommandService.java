package com.bank.loan.application.service;

import com.bank.loan.application.domain.EventStoreRecord;
import com.bank.loan.application.domain.LoanApplicationProjection;
import com.bank.loan.application.repository.EventStoreRepository;
import com.bank.loan.application.repository.LoanApplicationRepository;
import com.bank.loan.common.api.LoanApplicationResponse;
import com.bank.loan.common.api.SubmitLoanApplicationRequest;
import com.bank.loan.common.domain.LoanStatus;
import com.bank.loan.common.events.ApplicationSubmittedEvent;
import com.bank.loan.common.messaging.LoanTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class LoanApplicationCommandService {
    private final LoanApplicationRepository applicationRepository;
    private final EventStoreRepository eventStoreRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public LoanApplicationCommandService(LoanApplicationRepository applicationRepository,
                                         EventStoreRepository eventStoreRepository,
                                         KafkaTemplate<String, String> kafkaTemplate,
                                         ObjectMapper objectMapper) {
        this.applicationRepository = applicationRepository;
        this.eventStoreRepository = eventStoreRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public LoanApplicationResponse submit(SubmitLoanApplicationRequest request) {
        String applicationId = "LA-" + UUID.randomUUID();
        ApplicationSubmittedEvent event = new ApplicationSubmittedEvent(
                UUID.randomUUID(),
                applicationId,
                request.customerId(),
                request.product(),
                request.requestedAmount(),
                request.tenureMonths(),
                request.declaredMonthlyIncome(),
                Instant.now()
        );

        LoanApplicationProjection projection = new LoanApplicationProjection();
        projection.setApplicationId(applicationId);
        projection.setCustomerId(request.customerId());
        projection.setProduct(request.product());
        projection.setRequestedAmount(request.requestedAmount());
        projection.setStatus(LoanStatus.APPLICATION_SUBMITTED);
        projection.setUpdatedAt(event.occurredAt());
        applicationRepository.save(projection);
        publishAndStore(event.applicationId(), event.eventType(), event);

        return new LoanApplicationResponse(applicationId, request.customerId(), LoanStatus.APPLICATION_SUBMITTED,
                request.requestedAmount(), null, "Application accepted for asynchronous underwriting");
    }

    private void publishAndStore(String aggregateId, String eventType, Object event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            eventStoreRepository.save(new EventStoreRecord(aggregateId, eventType, payload, Instant.now()));
            kafkaTemplate.send(LoanTopics.APPLICATION_SUBMITTED, aggregateId, payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize application event", ex);
        }
    }
}
