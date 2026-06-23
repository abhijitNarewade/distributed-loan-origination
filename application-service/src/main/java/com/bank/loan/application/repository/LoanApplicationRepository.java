package com.bank.loan.application.repository;

import com.bank.loan.application.domain.LoanApplicationProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationRepository extends JpaRepository<LoanApplicationProjection, String> {
}
