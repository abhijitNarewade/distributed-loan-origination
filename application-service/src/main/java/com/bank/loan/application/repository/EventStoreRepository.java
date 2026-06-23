package com.bank.loan.application.repository;

import com.bank.loan.application.domain.EventStoreRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventStoreRepository extends JpaRepository<EventStoreRecord, Long> {
}
