# Kafka Topic Design

## Topic Catalog

| Topic | Producer | Consumers | Partitions | Retention |
|---|---|---|---:|---|
| `loan.application.submitted.v1` | application-service | customer-service | 6 | compacted + retention |
| `loan.customer.verified.v1` | customer-service | kyc-service | 6 | compacted + retention |
| `loan.kyc.completed.v1` | kyc-service | bureau-service | 6 | compacted + retention |
| `loan.bureau.completed.v1` | bureau-service | risk-service | 6 | compacted + retention |
| `loan.risk.assessed.v1` | risk-service | approval-service | 6 | compacted + retention |
| `loan.approval.approved.v1` | approval-service | disbursement-service | 6 | compacted + retention |
| `loan.approval.rejected.v1` | policy services | notification/manual review | 6 | 7 years in regulated archive |
| `loan.disbursement.completed.v1` | disbursement-service | servicing/core ledger | 6 | 7 years in regulated archive |
| `loan.dead-letter.v1` | all services | operations | 6 | 30 days |

## Partitioning

All business topics are keyed by `applicationId`. This preserves ordering for a single loan application while allowing horizontal scale across applications.

## Reliability Rules

- Consumers must be idempotent by `eventId`.
- Producers should use idempotent producer settings in production.
- Events are immutable business facts.
- Contract changes require a new version or backward-compatible schema evolution.
- Dead-letter events must carry original topic, partition, offset, exception type, and stack trace hash.
