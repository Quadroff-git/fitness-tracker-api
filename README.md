# fitness-tracker-api
A Spring Boot REST API test task made as a part of traineeship application at Modsen

## Deploying
The repo includes a configured docker compose file.
```
docker compose up -d --build
```

## API documentation
OpenAPI 3 documentation is generated automatically on build with `springdoc`.
The documentation is available at:
- `your-server-name:8080/v3/api-docs` for JSON
- `your-server-name:8080/v3/api-docs.yaml` for YAML

Swagger UI is available at `your-server-name:8080/swagger-ui.html`

## Current state
The following requirements are met:
- All main API endpoints fully implemented, as well as an authentication API.
- JWT authentication implemented using JJWT for token generation
- Flexible filtering, sorting and pagination implemented for the main GET endpoint
- Jakarta Validation used
- Exception handling at controller level is centralized and implemented in `@ControllerAdvice` annotated classes at
`org.pileka.exception.handler`
- The code adheres to SOLID and is split into proper 3-layer architecture, as well as a number of additional classes.
Base level interfaces were extracted wherever feasible
- DTO objects used throughout the code
- Business logic (service layer classes) test coverage is 93% or higher for all metrics
- GitFlow workflow used
- Conventional Commits specification followed
- Docker containerization set up
- Swagger (OpenAPI) documentation generation set up. 
