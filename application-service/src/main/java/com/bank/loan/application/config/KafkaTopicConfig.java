package com.bank.loan.application.config;

import com.bank.loan.common.messaging.LoanTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    NewTopic applicationSubmitted() {
        return topic(LoanTopics.APPLICATION_SUBMITTED);
    }

    @Bean
    NewTopic customerVerified() {
        return topic(LoanTopics.CUSTOMER_VERIFIED);
    }

    @Bean
    NewTopic kycCompleted() {
        return topic(LoanTopics.KYC_COMPLETED);
    }

    @Bean
    NewTopic bureauCompleted() {
        return topic(LoanTopics.BUREAU_COMPLETED);
    }

    @Bean
    NewTopic riskAssessed() {
        return topic(LoanTopics.RISK_ASSESSED);
    }

    @Bean
    NewTopic loanApproved() {
        return topic(LoanTopics.LOAN_APPROVED);
    }

    @Bean
    NewTopic loanRejected() {
        return topic(LoanTopics.LOAN_REJECTED);
    }

    @Bean
    NewTopic loanDisbursed() {
        return topic(LoanTopics.LOAN_DISBURSED);
    }

    private NewTopic topic(String name) {
        return TopicBuilder.name(name).partitions(6).replicas(1).compact().build();
    }
}
