package com.bank.loan.common.api;

import com.bank.loan.common.domain.LoanStatus;

import java.math.BigDecimal;

public record LoanApplicationResponse(
        String applicationId,
        String customerId,
        LoanStatus status,
        BigDecimal requestedAmount,
        BigDecimal approvedAmount,
        String message
) {
}
