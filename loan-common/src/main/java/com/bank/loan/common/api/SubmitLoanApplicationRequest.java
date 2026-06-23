package com.bank.loan.common.api;

import com.bank.loan.common.domain.LoanProduct;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SubmitLoanApplicationRequest(
        @NotBlank String customerId,
        @NotNull LoanProduct product,
        @NotNull @DecimalMin("1000.00") BigDecimal requestedAmount,
        @Min(6) int tenureMonths,
        @NotNull @DecimalMin("1.00") BigDecimal declaredMonthlyIncome,
        @NotBlank String purpose
) {
}
