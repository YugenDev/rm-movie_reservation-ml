# Try it yourself ;)

## Run it with docker compose

```bash
docker-compose up --build
```

# Languages and Frameworks

### 1. **User Service**
- **Language/Framework**: FastAPI (Python)
- **Design Pattern**: **Repository Pattern**
  - **Description**: Separates data access logic from business logic, allowing changes to the database implementation without affecting the rest of the service.
- **Architecture**: **Layered Architecture**
  - **Description**: Structures the code in layers (controllers, services, repositories), making maintenance and testing easier.

### 2. **Movie Service**
- **Language/Framework**: Go (Echo)
- **Design Pattern**: **Command Query Responsibility Segregation (CQRS)**
  - **Description**: Separates read and write operations, allowing each operation to be optimized according to its needs. This is useful if you plan to have many complex queries or reports.
- **Architecture**: **Microkernel Architecture**
  - **Description**: Allows adding new functionalities (such as new movie categories) without affecting the core of the system.

### 3. **Showtime Service**
- **Language/Framework**: Spring WebFlux (Java)
- **Design Pattern**: **Event Sourcing**
  - **Description**: Stores state changes as a series of events, making it easier to retrieve the current state and audit. It is especially useful in services that handle frequently changing data.
- **Architecture**: **Reactive Architecture**
  - **Description**: Based on reactive principles to handle concurrency and scalability, making it ideal for applications requiring high availability.

### 4. **Reservation Service**
- **Language/Framework**: Micronaut (Java)
- **Design Pattern**: **Saga Pattern**
  - **Description**: Manages distributed transactions through a series of coordinated steps. It allows rollback of actions if a part of the reservation fails (for example, if certain seats cannot be booked).
- **Architecture**: **Hexagonal Architecture (Ports and Adapters)**
  - **Description**: Facilitates interaction with the system through well-defined interfaces, allowing easy integration with other services and external systems.

---

This combination of design patterns and specific architectures for each microservice will not only leverage the strengths of the chosen languages and frameworks but will also facilitate the maintainability, scalability, and evolution of the system.
