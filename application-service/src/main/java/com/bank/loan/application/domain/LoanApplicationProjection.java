package com.bank.loan.application.domain;

import com.bank.loan.common.domain.LoanProduct;
import com.bank.loan.common.domain.LoanStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "loan_application_projection")
public class LoanApplicationProjection {
    @Id
    private String applicationId;
    private String customerId;
    @Enumerated(EnumType.STRING)
    private LoanProduct product;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private Instant updatedAt;

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public LoanProduct getProduct() { return product; }
    public void setProduct(LoanProduct product) { this.product = product; }
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
