# Distributed Loan Origination Platform

Production-grade reference architecture for a retail banking loan origination platform using Java 21, Spring Boot, Kafka, PostgreSQL, Redis, Docker, Kubernetes, OpenTelemetry, DDD, CQRS, event sourcing, and saga orchestration.

## Services

| Service | Port | Role |
|---|---:|---|
| api-gateway | 8080 | Public ingress |
| service-discovery | 8761 | Eureka registry |
| application-service | 8081 | Application command API, event store, projection |
| customer-service | 8082 | Customer verification |
| kyc-service | 8083 | AML/sanctions screening |
| bureau-service | 8084 | Credit bureau abstraction |
| risk-service | 8085 | Risk scoring and policy |
| approval-service | 8086 | Approval decisioning and read model |
| disbursement-service | 8087 | Core banking disbursement |

## Architecture

Read these first:

- [System architecture](docs/architecture.md)
- [Event flow diagrams](docs/event-flow.md)
- [Service contracts](docs/service-contracts.md)
- [Kafka topic design](docs/kafka-topic-design.md)

## Build

```bash
mvn clean verify
```

## Run Locally

Build service jars first:

```bash
mvn clean package -DskipTests
docker compose up --build
```

Submit a loan application through the gateway:

```bash
curl -X POST http://localhost:18080/api/v1/applications \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-1001",
    "product": "PERSONAL_LOAN",
    "requestedAmount": 500000,
    "tenureMonths": 36,
    "declaredMonthlyIncome": 120000,
    "purpose": "Home renovation"
  }'
```

Query the application:

```bash
curl http://localhost:18080/api/v1/applications/{applicationId}
```

Query approval:

```bash
curl http://localhost:18080/api/v1/approvals/{applicationId}
```

Default host ports are chosen to avoid collisions with the other banking demos:

- API Gateway: `http://localhost:18080`
- Eureka: `http://localhost:18761`
- Kafka: `localhost:29092`
- PostgreSQL: `localhost:15432`
- Redis: `localhost:26379`
- OpenTelemetry: `localhost:24317`, `localhost:24318`, and `localhost:28889`

## Deploy to Kubernetes

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/platform-stateful.yaml
kubectl apply -f k8s/services.yaml
kubectl apply -f k8s/event-processors.yaml
```

Replace the sample `ghcr.io/example/...` image names in `k8s/` with your registry paths.

## Interview Talking Points

- Choreography saga avoids central orchestration coupling, but requires strong observability and idempotency.
- Event sourcing gives auditability for regulated lending decisions.
- CQRS separates write workflows from decision/query models.
- Kafka topics are versioned and partitioned by `applicationId` for per-application ordering.
- PostgreSQL is used for durable state; Redis is reserved for idempotency, distributed locks, and hot reference data.
- OpenTelemetry enables trace correlation across HTTP and Kafka stages.
- Production evolution should add schema registry, transactional outbox, mTLS, OAuth2, field-level PII encryption, and manual underwriting workflows.
