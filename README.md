# fitness-tracker-api
A Spring Boot REST API test task made as a part of traineeship application at Modsen

## Building
```
mvn clean package
```

## Current state
The following requirements are met:
- All main API endpoints fully implemented, as well as a minimal authentication API.
- JWT authentication implemented using JJWT for token generation
- Flexible filtering, sorting and pagination implemented for the main GET endpoint
- Jakarta Validation used
- Exception handling at controller level is centralized and implemented in `@ControllerAdvice` annotated classes at
`org.pileka.exception.handler`
- The code adheres to SOLID and is split into proper 3-layer architecture, as well as a number of additional classes.
Base level interfaces and implementations were extracted wherever feasible
- DTO objects used throughout the code
- GitFlow workflow used as much as possible (most feature branches were local and shortlived and there have been no 
releases yet, so there isn't much to back that up)
- Conventional Commits specification followed

Things that are not done yet:
- Test coverage is not sufficient and the implemented tests require some refinement (mostly done though!)
- Swagger (OpenAPI) documentation generation not set up
- Docker containerization