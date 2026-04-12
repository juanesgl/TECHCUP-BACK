# Backend para TECHCUP

---

##  Integrantes del Equipo

| Nombre                                                                   |
|--------------------------------------------------------------------------|
| [Andrea Mariana Parra Urrego](https://github.com/marianaparraurrego-oss) |
| [Isaac David Burgos Cervantes](https://github.com/Isaac1805BC)           |
| [Juan Esteban Sanchez Garcia](https://github.com/juanesgl)               |
| [Laura Valentina Santiago Marquez](https://github.com/LauLau1214)        |
| [Zharik Natalia Mahecha Castillo](https://github.com/199n1)              |

## 🛠️ Tecnologías Usadas
- **Java 21**: Lenguaje de programación.
- **Spring Boot 3**: Framework principal para la arquitectura backend (Spring Web, Spring Data JPA, Spring Security).
- **PostgreSQL**: Motor de base de datos relacional.
- **Maven**: Gestor de dependencias y construcción.
- **JUnit 5 & Mockito**: Herramientas principales para Testing.
- **JaCoCo**: Generación de reportes de cobertura de código.
- **SonarQube**: Análisis estático.
- **Swagger UI (OpenAPI)**: Documentación interactiva de la API REST.
- **Lombok**: Eliminación de código repetitivo.

---

## 🚀 Sprints del Proyecto

| Sprint   | Objetivo principal                                                                                              | Enlace                                      |
|----------|-----------------------------------------------------------------------------------------------------------------|---------------------------------------------|
| Sprint 1 | Estructura base MVC, backlog JIRA, diagramas de arquitectura iniciales, implementación sin persistencia         | [ Ver Sprint 1](sprints/Sprint-1/README.md) |
| Sprint 2 | Deuda técnica Sprint-1, Swagger, Logs, diagramas de secuencia, pruebas con Jacoco y SonarQube                   | [Ver Sprint 2](sprints/Sprint-2/README.md)  |
| Sprint 3 | Deuda técnica Sprint-2, persistencia, seguridad API (JWT + OAuth2 + Roles + SSL/TLS), diagrama Entidad-relacion | [Ver Sprint 3](sprints/Sprint-3/README.md)  |

---

## Docker

Documentación completa (arquitectura, variables, troubleshooting, Azure vs local): **[docs/dockerizacion.md](docs/dockerizacion.md)**.

### Uso rápido

1. Copiar `.env.example` a `.env` y definir `POSTGRES_PASSWORD` y `JWT_SECRET`.
2. Construir la imagen del backend y levantar API + PostgreSQL:

```bash
docker build -t techcup-backend:local .
docker compose -f compose.yml up -d --build
```

3. Comprobar: `http://localhost:8085/swagger-ui.html` (o el puerto configurado en `APP_PORT`).

Validación opcional: `.\scripts\validate-docker-stack.ps1` (Windows) o `./scripts/validate-docker-stack.sh` (Linux/macOS).

