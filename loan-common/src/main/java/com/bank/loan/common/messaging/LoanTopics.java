package com.bank.loan.common.messaging;

public final class LoanTopics {
    public static final String APPLICATION_SUBMITTED = "loan.application.submitted.v1";
    public static final String CUSTOMER_VERIFIED = "loan.customer.verified.v1";
    public static final String KYC_COMPLETED = "loan.kyc.completed.v1";
    public static final String BUREAU_COMPLETED = "loan.bureau.completed.v1";
    public static final String RISK_ASSESSED = "loan.risk.assessed.v1";
    public static final String LOAN_APPROVED = "loan.approval.approved.v1";
    public static final String LOAN_REJECTED = "loan.approval.rejected.v1";
    public static final String LOAN_DISBURSED = "loan.disbursement.completed.v1";
    public static final String DEAD_LETTER = "loan.dead-letter.v1";

    private LoanTopics() {
    }
}
