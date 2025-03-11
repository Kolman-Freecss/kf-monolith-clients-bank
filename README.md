# KF Bank - Client Management API

A modern banking client management REST API built with a hybrid approach combining Domain-Driven Design principles and
hexagonal architecture (ports and adapters).

## 🏦 Architecture Overview

### Hybrid DDD & Hexagonal Architecture

The application implements a hybrid approach that combines the benefits of Domain-Driven Design's strategic patterns
with hexagonal architecture's technical organization:

- **Domain Layer** (Core Hexagon)
    - Organized by subdomains and bounded contexts
    - Contains business logic and rules
    - Pure Java, no frameworks or external dependencies
    - Uses DDD tactical patterns within each subdomain

- **Application Layer** (Use Cases)
    - Organized by subdomains matching the domain layer
    - Orchestrates the flow of data and domain objects
    - Implements use cases using domain objects
    - Defines ports (interfaces) for external adapters

- **Infrastructure Layer** (Adapters)
    - Implements the interfaces defined by ports
    - Contains framework-specific code
    - Handles external concerns (persistence, API, etc.)
    - Adapters can be shared across subdomains when appropriate

### Strategic Design & Subdomains

The project is organized into distinct subdomains, each representing a specific area of the business:

1. **Client Management (Core Subdomain)**
    - Handles client onboarding and profile management
    - Manages client verification and status
    - Core business differentiator

2. **Account Management (Supporting Subdomain)**
    - Manages account operations and status
    - Handles balance tracking
    - Essential but not unique to the business

3. **Transaction Processing (Generic Subdomain)**
    - Handles financial transactions
    - Follows standard banking practices
    - Could potentially be replaced by a third-party solution

### Domain-Driven Design Implementation

The project implements DDD tactical patterns within each subdomain:

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

4. **Domain Events**
    - Represent significant domain occurrences
    - Enable loose coupling between aggregates
    - Used for cross-subdomain communication

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
- **Cache**: Redis
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
│       ├── domain/                           # Domain Layer (Core Hexagon)
│       │   ├── clientmanagement/            # Client Management Subdomain
│       │   │   ├── model/                   # Domain Models
│       │   │   │   ├── Client.java         # Aggregate Root
│       │   │   │   ├── ClientStatus.java
│       │   │   │   └── vo/
│       │   │   └── repository/              # Repository Interfaces
│       │   ├── accountmanagement/           # Account Management Subdomain
│       │   │   ├── model/
│       │   │   │   ├── Account.java        # Aggregate Root
│       │   │   │   └── vo/
│       │   │   └── repository/
│       │   ├── transactionprocessing/       # Transaction Processing Subdomain
│       │   │   ├── model/
│       │   │   │   ├── Transaction.java
│       │   │   │   └── vo/
│       │   │   └── repository/
│       │   └── common/                      # Shared Kernel
│       │       └── vo/
│       │
│       ├── application/                     # Application Layer
│       │   ├── clientmanagement/           # Client Management Use Cases
│       │   │   ├── ports/
│       │   │   │   ├── input/
│       │   │   │   └── output/
│       │   │   └── services/
│       │   ├── accountmanagement/          # Account Management Use Cases
│       │   │   ├── ports/
│       │   │   └── services/
│       │   └── transactionprocessing/      # Transaction Processing Use Cases
│       │       ├── ports/
│       │       └── services/
│       │
│       ├── infrastructure/                  # Infrastructure Layer (Pure Hexagonal)
│       │   ├── adapters/
│       │   │   ├── in/                     # Input/Driving Adapters
│       │   │   │   ├── rest/              # REST Controllers
│       │   │   │   └── messaging/         # Message Consumers
│       │   │   └── out/                    # Output/Driven Adapters
│       │   │       ├── persistence/       # Database Repositories
│       │   │       └── messaging/         # Message Publishers
│       │   └── config/                     # Infrastructure Configuration
│       │
│       └── shared/                         # Shared/Common Components
│           ├── config/                     # Global Configuration
│           └── exceptions/                 # Global Exception Handling
```

## 🔧 Setup and Installation

1. Prerequisites
    - Java 17
    - Maven
    - PostgreSQL
    - Docker and Docker Compose (for Redis)

2. Clone the repository

```bash
git clone https://github.com/yourusername/kf-bank.git
```

3. Start Redis using Docker Compose

```bash
docker-compose up -d
```

This will start Redis on port 6379. You can verify Redis is running with:
```bash
docker-compose ps
```

4. Configure database

```bash
# Create database
createdb kfbank
```

5. Configure application.yml

```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
```

6. Run the application

```bash
mvn spring-boot:run
```

## 📝 API Documentation

Once the application is running, you can access the API documentation at:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Available Endpoints

### GraphQL Endpoints

- **GraphQL API**: `http://localhost:8080/graphql`
  - Supports both GET and POST methods
  - Available operations:
    ```graphql
    # Queries
    query {
      hello                     # Test query that returns a greeting
      getClientRights(clientId: ID!) {
        clientId
        rights
      }
    }

    # Mutations
    mutation {
      updateClientRights(clientId: ID!, rights: [String!]!) {
        clientId
        rights
      }
      invalidateClientRightsCache(clientId: ID!)    # Returns Boolean
      invalidateAllClientRightsCache                # Returns Boolean
    }
    ```

- **GraphiQL Interface**: `http://localhost:8080/graphiql`
  - Interactive GraphQL IDE for testing queries
  - Available in development mode

### REST API Documentation

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
  - Interactive API documentation
  - Try-out functionality enabled

- **OpenAPI Specification**: `http://localhost:8080/api-docs`
  - Raw OpenAPI/Swagger specification

### Monitoring & Management

- **Actuator Endpoints**: `http://localhost:8080/actuator`
  - `/health` - Application health information
  - `/info` - Application information
  - `/metrics` - Application metrics
  - `/prometheus` - Prometheus format metrics

### Development Tools

- **H2 Console**: `http://localhost:8080/h2-console`
  - Database: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

### Cache Management (Redis)

Redis is available on:
- Host: `localhost` (in Docker: `redis`)
- Port: `6379`

## CORS Configuration

The following origins are allowed:
- `http://localhost:8080`
- `http://localhost:3000`

Allowed methods:
- For GraphQL: GET, POST, PUT, DELETE, OPTIONS
- For GraphiQL: GET, POST, OPTIONS

## Environment Configuration

Key application properties:
- Server port: 8080
- Spring profiles: Default profile active
- Database: H2 in-memory database
- Cache: Redis
- GraphQL: Enabled with GraphiQL interface

## Monitoring & Management Endpoints (Actuator)

### 1. Health and Status
- **Health Check**: `GET /actuator/health`
  - Overall application health
  - Individual component status
  - Custom health indicators
- **Application Info**: `GET /actuator/info`
  - Environment information
  - Git details
  - Java runtime
  - OS information

### 2. Metrics and Performance
- **General Metrics**: `GET /actuator/metrics`
  - List of available metrics
- **Specific Metric**: `GET /actuator/metrics/{metric.name}`
  - Detailed metric information
- **Method Performance**:
  - `GET /actuator/metrics/method.execution.time`
  - `GET /actuator/metrics/method.execution.time.histogram`
  - `GET /actuator/metrics/method.execution.time.percentiles`
- **Prometheus Format**: `GET /actuator/prometheus`
  - Metrics in Prometheus format

### 3. Web and HTTP
- **HTTP Traces**: `GET /actuator/httptrace`
  - Recent HTTP request-response exchanges
- **Request Mappings**: `GET /actuator/mappings`
  - All @RequestMapping paths
  - Handler methods information

### 4. Application Configuration
- **Environment**: `GET /actuator/env`
  - Environment properties
  - Property sources
- **Configuration Properties**: `GET /actuator/configprops`
  - @ConfigurationProperties
- **Beans**: `GET /actuator/beans`
  - Spring bean list
  - Dependencies
- **Auto-configuration**: `GET /actuator/conditions`
  - Auto-configuration report
- **Scheduled Tasks**: `GET /actuator/scheduledtasks`
  - Scheduled task details

### 5. Cache Management
- **Caches Overview**: `GET /actuator/caches`
  - Cache names
  - Cache managers
- **Specific Cache**: `GET /actuator/caches/{cache.name}`
  - Individual cache details

### 6. Thread and Memory
- **Thread Dump**: `GET /actuator/threaddump`
  - JVM thread dump
- **Heap Dump**: `GET /actuator/heapdump`
  - JVM heap dump (downloadable)

### 7. Logging
- **Loggers**: `GET /actuator/loggers`
  - Logger configurations
- **Specific Logger**: `GET /actuator/loggers/{logger.name}`
  - Individual logger settings
  - Runtime log level management

### Security Note
In production environments:
- Restrict access to sensitive endpoints
- Enable authentication
- Consider using a separate management port
- Disable unnecessary endpoints

### Usage Examples

1. Check application health:
```bash
curl http://localhost:8080/actuator/health
```

2. View method performance metrics:
```bash
curl http://localhost:8080/actuator/metrics/method.execution.time
```

3. Get Prometheus metrics:
```bash
curl http://localhost:8080/actuator/prometheus
```

4. Change logger level:
```bash
curl -X POST http://localhost:8080/actuator/loggers/org.springframework \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'
```
