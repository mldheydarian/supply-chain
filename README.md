# Supply Chain Project

**Supply Chain** is a Spring Boot 3.3 microservice for managing products and movements, with modular architecture for maintainability and testability. It features paging, filtering, global exception handling, and optimistic locking for entity updates.

**Tech Stack:**
- **Backend:** Java 17, Spring Boot
- **Database:** PostgreSQL (prod), H2 (test)
- **Data Access:** Spring Data JPA, Hibernate
- **Validation & Mapping:** Jakarta Validation, MapStruct + Lombok
- **API Docs:** Springdoc OpenAPI (Swagger UI)
- **Containerization:** Docker & Docker Compose

**Testing & Design Notes:**
- Unit & integration tests run on H2
- Optimistic locking prevents concurrent update conflicts
- DTOs (spec objects) separate API and service layers
- Unified exception responses via `GeneralResponse` / `ResponseService`
  
**Running the Project:**  
The easiest way to run the project is via Docker Compose. After cloning the repository:

```bash
git clone https://github.com/mldheydarian/supply-chain.git
cd supply-chain
docker-compose up --build -d
```

You can access the Swagger UI at http://localhost:8090/swagger-ui/index.html
.
