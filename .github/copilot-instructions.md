# Project Guidelines

## Code Style
- Language: Java 21 with Spring Boot 3.4.x.
- Keep package structure aligned with the current layered organization under src/main/java/edu/dosw/proyect.
- Prefer constructor injection for services/controllers.
- Keep controllers thin: input mapping + response shaping only; business rules belong in services.
- Preserve existing naming when editing an existing domain area (the repository mixes Spanish and English domain names).

## Architecture
- Entry point: src/main/java/edu/dosw/proyect/TechcupFutbolApplication.java.
- Main layers and boundaries:
  - controllers: REST endpoints, request/response DTOs, and mappers.
  - core/services: business logic.
  - core/repositories: Spring Data JPA persistence.
  - core/models: JPA entities and enums.
  - core/exceptions: centralized error handling.
  - core/utils: cross-cutting utility patterns.
- Prefer the flow Controller -> DTO/Mapper -> Service -> Repository -> Model.
- Keep DTOs in controllers/dtos and mappers in controllers/mappers to match existing project conventions.

## Build And Test
- Local build: mvn clean package
- Run app (dev): mvn spring-boot:run
- Run tests: mvn test
- Coverage/report lifecycle: mvn verify
- Optional Sonar analysis: mvn sonar:sonar
- Docker database: docker-compose up
- App defaults:
  - HTTP port: 8085
  - PostgreSQL expected at 127.0.0.1:5432 (techcup_db)

## Conventions
- Do not edit generated outputs in target/.
- Keep Swagger/OpenAPI endpoints available (see security config and springdoc paths).
- There are parallel domain names (for example Jugador/Player, Equipo/Team). Reuse the naming style already used in the feature area you are modifying instead of renaming broadly.
- When adding endpoints, keep route style consistent with existing /api/* patterns.

## Pitfalls
- Security config currently permits login/register, swagger, h2-console, and debug routes; avoid unintentionally changing access behavior.
- Repository includes sprint evidence and docs with mixed language content (Spanish/English); prefer linking to existing docs instead of duplicating explanations.
- Sprint 2 readme currently contains unresolved merge markers. Do not copy content from conflicted sections without verification.

## References
- Project overview: README.md
- Sprint docs: Sprint/Sprint-1/Readme.md, Sprint/Sprint-2/Readme.md, Sprint/Sprint-3/Readme.md
- Runtime config: src/main/resources/application.properties
- Infra config: docker-compose.yml
- Maven config: pom.xml
