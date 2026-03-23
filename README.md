![CI](https://github.com/zinchuknadia/openbanking-api-task/actions/workflows/ci.yml/badge.svg)
# Open Banking API

A simple Open Banking API built with Spring Boot that allows users to:

- Retrieve account balance
- Fetch recent transactions
- Initiate payments

The system integrates with a mocked external banking service to simulate real-world Open Banking (PSD2) interactions.

## Features

- Get account balance by IBAN
- Get last 10 transactions
- Initiate payment between accounts
- External bank integration (mocked API)
- Payment lifecycle management (CREATED → ACCEPTED → INITIATED/FAILED)
- Input validation using Jakarta Bean Validation
- Global exception handling
- Logging
- Unit tests
- Integration tests
- CI pipeline (GitHub Actions)
- Swagger documentation

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database
- RestTemplate
- Maven
- JUnit & Mockito

## Architecture

The application follows a layered architecture:

Controller → Service → Repository → Database  
Controller → Service → ExternalBankClient → Mock Bank API

- Controller: handles HTTP requests
- Service: contains business logic
- Repository: interacts with database
- ExternalBankClient: communicates with external bank API

## API Endpoints

### Get balance
GET /api/accounts/{iban}/balance

### Get transactions
GET /api/accounts/{iban}/transactions

### Initiate payment
POST /api/payments/initiate

Request:
{
"fromIban": "UA123",
"toIban": "DE456",
"amount": 100,
"currency": "EUR"
}

## Running the Application

1. Clone the repository:

git clone https://github.com/zinchuknadia/openbanking-api-task

2. Navigate to the project:

cd openbanking-api-task

3. Run the application:

mvn spring-boot:run

Application will start on:
http://localhost:8081/swagger-ui/index.html/

## Database

The application uses an in-memory H2 database.

H2 Console:
http://localhost:8081/h2-console

JDBC URL: jdbc:h2:mem:testdb  
User: sa  
Password: (empty)

## Testing

Run tests with:

mvn test

Includes:
- Unit tests for services
- Mocking external bank client
- Integration tests for database operations