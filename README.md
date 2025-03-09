# KF Bank - Client Management API

A modern banking client management REST API built with hexagonal architecture and DDD principles.

## 🏦 Domain Description

### Bounded Contexts

#### Client Management Context
This is the core domain that handles all client-related operations and management.

##### Aggregates:

1. **Client Aggregate**
   - Client (Root Entity)
   - PersonalInformation (Value Object)
   - Address (Value Object)
   - ContactDetails (Value Object)

2. **Account Aggregate**
   - Account (Root Entity)
   - Balance (Value Object)
   - TransactionHistory (Entity)
   - AccountStatus (Value Object)

##### Entities:
1. **Client**
   - Manages client's core information
   - Handles client status and verification
   - Links to accounts and products

2. **Account**
   - Manages account operations
   - Tracks balance and status
   - Handles transaction history

3. **Transaction**
   - Records financial movements
   - Maintains transaction details
   - Links to accounts involved

## 🛠 Tech Stack

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **ORM**: Hibernate
- **API Style**: REST
- **Build Tool**: Maven

### Testing
- **Unit Testing**: JUnit 5
- **Integration Testing**: TestContainers
- **API Testing**: REST Assured

## 🚀 Features

- Client onboarding and management
- Account creation and management
- Transaction processing and history
- Real-time balance updates
- Audit logging
- Reporting and analytics

## 📦 Project Structure (Hexagonal Architecture)

```
src/
├── main/
│   ├── java/com/kfbank/
│   │   ├── application/          # Application services / Use cases
│   │   │   ├── ports/
│   │   │   │   ├── input/       # Input ports (interfaces)
│   │   │   │   └── output/      # Output ports (interfaces)
│   │   │   └── services/        # Use case implementations
│   │   ├── domain/              # Domain model and business logic
│   │   │   ├── client/          # Client aggregate
│   │   │   ├── account/         # Account aggregate
│   │   │   └── common/          # Shared kernel
│   │   └── infrastructure/      # Adapters and configurations
│   │       ├── adapters/
│   │       │   ├── input/       # REST Controllers
│   │       │   └── output/      # Repository implementations
│   │       └── config/          # Spring configurations
│   └── resources/
│       └── application.yml
└── test/
    └── java/com/kfbank/
        ├── unit/
        ├── integration/
        └── architecture/
```

## 🔧 Setup and Installation

1. Prerequisites
   - Java 17
   - Maven
   - PostgreSQL

2. Clone the repository
```bash
git clone https://github.com/yourusername/kf-bank.git
```

3. Configure database
```bash
# Create database
createdb kfbank
```

4. Configure application.yml
```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
```

5. Run the application
```bash
mvn spring-boot:run
```

## 📝 API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs