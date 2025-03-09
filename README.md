# KF Bank - Client Management API

A modern banking client management REST API built with hexagonal architecture (ports and adapters) and Domain-Driven Design principles.

## 🏦 Architecture Overview

### Hexagonal Architecture (Ports & Adapters)

The application follows the hexagonal architecture pattern, also known as "Ports and Adapters". This architecture style isolates the domain logic from external concerns:

- **Domain Layer** (Core Hexagon)
  - Contains business logic and rules
  - Pure Java, no frameworks or external dependencies
  - Completely isolated from external concerns
  - Uses DDD tactical patterns

- **Application Layer** (Use Cases)
  - Orchestrates the flow of data and domain objects
  - Implements use cases using domain objects
  - Defines ports (interfaces) for external adapters

- **Infrastructure Layer** (Adapters)
  - Implements the interfaces defined by ports
  - Contains framework-specific code
  - Handles external concerns (persistence, API, etc.)

### Domain-Driven Design Implementation

The project implements DDD tactical patterns:

1. **Aggregates**
   - Strong consistency boundaries
   - Accessed only through aggregate root
   - Example: `Client` aggregate includes `ClientStatus`

2. **Entities**
   - Have identity and lifecycle
   - Mutable objects
   - Example: `Transaction` in the Account context

3. **Value Objects**
   - Immutable
   - No identity
   - Example: `Address`, `Balance`

4. **Domain Events** (To be implemented)
   - Represent significant domain occurrences
   - Enable loose coupling between aggregates

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

## 📦 Project Structure

```
src/
├── main/
│   └── java/org/kolmanfreecss/
│       ├── domain/                  # Domain Layer (Core Hexagon)
│       │   ├── client/             # Client Bounded Context
│       │   │   ├── Client.java     # Aggregate Root
│       │   │   ├── ClientStatus.java
│       │   │   └── vo/
│       │   ├── account/            # Account Bounded Context
│       │   │   ├── Account.java    # Aggregate Root
│       │   │   ├── Transaction.java
│       │   │   └── vo/
│       │   └── common/             # Shared Kernel
│       │       └── vo/
│       │
│       ├── application/            # Application Layer
│       │   ├── ports/             # Ports (Interfaces)
│       │   │   ├── input/         # Primary/Driving Ports
│       │   │   └── output/        # Secondary/Driven Ports
│       │   └── services/          # Use Cases Implementation
│       │
│       └── infrastructure/         # Infrastructure Layer
│           ├── adapters/
│           │   ├── input/         # REST Controllers, etc.
│           │   └── output/        # Repository Implementations
│           └── config/
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

## 👥 Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.