# Service Contracts

## Public REST Contracts

### Submit Loan Application

`POST /api/v1/applications`

```json
{
  "customerId": "CUST-1001",
  "product": "PERSONAL_LOAN",
  "requestedAmount": 500000,
  "tenureMonths": 36,
  "declaredMonthlyIncome": 120000,
  "purpose": "Home renovation"
}
```

Response: `202 Accepted`

```json
{
  "applicationId": "LA-...",
  "customerId": "CUST-1001",
  "status": "APPLICATION_SUBMITTED",
  "requestedAmount": 500000,
  "approvedAmount": null,
  "message": "Application accepted for asynchronous underwriting"
}
```

### Query Application

`GET /api/v1/applications/{applicationId}`

Returns the application-service CQRS projection.

### Query Approval

`GET /api/v1/approvals/{applicationId}`

Returns the approval-service read model.

## Domain Event Contracts

All events include:

- `eventId`
- `applicationId`
- `customerId`
- `occurredAt`

| Event | Key Payload Fields |
|---|---|
| `ApplicationSubmitted` | product, requestedAmount, tenureMonths, declaredMonthlyIncome |
| `CustomerVerified` | decision, reasonCode |
| `KycCompleted` | amlDecision, sanctionsDecision, riskBand |
| `BureauCheckCompleted` | creditScore, activeTradelines, decision |
| `RiskAssessed` | probabilityOfDefault, debtToIncomeRatio, decision |
| `LoanApproved` | approvedAmount, annualInterestRate, approvalAuthority |
| `LoanRejected` | rejectionCode, rejectionReason |
| `LoanDisbursed` | coreBankingAccount, amount, paymentReference |
