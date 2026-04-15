# Dockerización del backend TECHCUP

Este documento describe **cómo está armada** la dockerización del backend (Spring Boot + PostgreSQL), **qué archivos intervienen** y **cómo usarla** en desarrollo local. 

---

## 1. Objetivos

- **Estandarizar** el entorno: misma versión de Java y mismas dependencias empaquetadas en la imagen.
- **Facilitar el despliegue**: artefacto reproducible (`docker build`) y stack completo con `docker compose`.
- **Portabilidad**: la misma imagen puede ejecutarse en otro host; solo cambian variables de entorno (p. ej. Azure vs local).

---

## 2. Arquitectura del stack

| Componente                          | Rol                                                                                                       |
|-------------------------------------|-----------------------------------------------------------------------------------------------------------|
| **`Dockerfile`**                    | Construye la imagen del **solo backend**: compila con Maven (Java 21) y ejecuta el JAR con JRE 21 Alpine. |
| **`compose.yml`**                   | Orquesta **dos servicios** en la misma red: `database` (PostgreSQL 15) y `app` (API).                     |
| **Red `techcup_net`**               | Red bridge: el servicio `app` resuelve el hostname **`database`** hacia el contenedor de Postgres.        |
| **Volumen `techcup_postgres_data`** | Persiste los datos de PostgreSQL entre reinicios del contenedor.                                          |
| **`.env` (local, no versionado)**   | Credenciales y JWT; se copia desde `.env.example`.                                                        |

Flujo resumido:

1. El contenedor `database` arranca y pasa el **healthcheck** (`pg_isready`).
2. El contenedor `app` espera a que `database` esté sano (`depends_on` + `condition: service_healthy`).
3. Compose **sobrescribe** la URL JDBC del servicio `app` para usar `jdbc:postgresql://database:5432/<POSTGRES_DB>` (no `localhost`).

---

## 3. Dockerfile (backend únicamente)

Ubicación: raíz del proyecto, `Dockerfile`.

- **Etapa `build`**: imagen `maven:3.9.9-eclipse-temurin-21-alpine`, copia `pom.xml` y `src/`, ejecuta `mvn -B -DskipTests package`.
- **Etapa final**: `eclipse-temurin:21-jre-alpine`, copia el JAR generado (`target/*.jar`) como `/app/app.jar`.
- Usuario no privilegiado (`spring`), puerto **8085**, `JAVA_OPTS` opcional para tuning en runtime.

El archivo **`.dockerignore`** reduce el contexto de build (excluye `target/`, `.git`, `.env`, etc.) para builds más rápidos y sin filtrar secretos accidentales desde el contexto.

---

## 4. Variables de entorno y secretos

### Principio

- **No** se versionan contraseñas de base de datos ni **JWT** en el código fuente.
- `src/main/resources/application.properties` usa placeholders: `SPRING_DATASOURCE_*`, `JWT_SECRET`, etc.
- Valores reales: archivo **`.env`** en la raíz (listado en `.gitignore`). Plantilla: **`.env.example`**.

### Comportamiento con Compose

- `env_file: .env` inyecta variables en los contenedores.
- Para el servicio **`app`**, la sección `environment` de `compose.yml` **define** la URL JDBC interna (`database` como host) y alinea usuario/contraseña con `POSTGRES_*`.

### Desarrollo sin contenedor de la API

Si se ejecuta la API con **Maven o el IDE** y solo se usa Postgres en Docker, en `.env` se debe figurar una URL hacia **localhost** y el puerto publicado (p. ej. `jdbc:postgresql://localhost:5433/...`), alineada con `POSTGRES_PORT`.

---

## 5. Cómo construir la imagen del backend

Desde la raíz del backend (`TECHCUP-BACK`):

```bash
docker build -t techcup-backend:local .
```

Eso equivale a la imagen referenciada en `compose.yml` (`image: techcup-backend:local`); Compose también puede construirla al hacer `up --build`.

---

## 6. Cómo levantar los contenedores

### Requisitos

- Docker Engine y Docker Compose v2.
- Docker Desktop (Windows/macOS) **en ejecución**.
- Archivo `.env` creado a partir de `.env.example` (valores de `POSTGRES_PASSWORD` y `JWT_SECRET` propios).

### Pasos

```bash
cp .env.example .env
# Editar .env

docker compose -f compose.yml up -d --build
```

En directorios donde el archivo se llama `compose.yml`, también se puede usar:

```bash
docker compose up -d --build
```

(si Compose detecta `compose.yml` en el directorio actual).

### URLs útiles tras el arranque

| Recurso                  | URL típica                                                                     |
|--------------------------|--------------------------------------------------------------------------------|
| API                      | `http://localhost:8085` (o el puerto `APP_PORT` definido en el archivo `.env`) |
| Swagger UI               | `http://localhost:8085/swagger-ui.html`                                        |
| OpenAPI JSON             | `http://localhost:8085/v3/api-docs`                                            |
| PostgreSQL desde el host | `localhost` y puerto `POSTGRES_PORT` (por defecto `5433`)                      |

### Comandos de operación

```bash
docker compose -f compose.yml ps
docker compose -f compose.yml logs -f app
docker compose -f compose.yml down
```

`down` detiene contenedores; el volumen de Postgres **persiste** salvo que se borre el volumen explícitamente.

---

## 7. Validación del entorno local

1. Contenedores en ejecución: `docker compose ps` (estado `running` / `healthy` para `database`).
2. Logs sin errores de arranque Spring: `docker compose logs app`.
3. Comprobación HTTP del OpenAPI (ajusta puerto si cambiaste `APP_PORT`):


---

## 8. Problemas frecuentes

| Síntoma                                                            | Qué revisar                                                                                                                                                                                              |
|--------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `failed to connect to the docker API` / `dockerDesktopLinuxEngine` | Docker Desktop no está iniciado o el daemon no está listo. Abrir Docker Desktop y esperar a “Running”; luego `docker ps`.                                                                                |
| La API no conecta a la BD                                          | Dentro de Compose la URL debe usar el host `database`. Revisá que no estés forzando en `.env` una URL a `localhost` **dentro** del contenedor `app` (Compose debe poder sobrescribir con `environment`). |
| Puerto en uso                                                      | Cambiá `APP_PORT` o `POSTGRES_PORT` en `.env`.                                                                                                                                                           |

---

## 9. Base de datos local (Docker) vs remota (Azure)

| Escenario                            | Dónde corre la API         | Conexión a PostgreSQL                                                                                        |
|--------------------------------------|----------------------------|--------------------------------------------------------------------------------------------------------------|
| Stack local con Compose              | Contenedor `app`           | `jdbc:postgresql://database:5432/<db>` (inyectado por Compose)                                               |
| API en Maven/IDE + solo BD en Docker | Host                       | `jdbc:postgresql://localhost:<POSTGRES_PORT>/<db>` en `.env`                                                 |
| QA / Producción (Azure)              | App Service, AKS, VM, etc. | Variables en el servicio o **Azure Key Vault**; JDBC a `*.postgres.database.azure.com` con `sslmode=require` |

Guía ampliada de Azure: [azure-postgresql-setup.md](./azure-postgresql-setup.md).

---

## 10. Mapa de archivos

| Archivo                                     | Descripción                                |
|---------------------------------------------|--------------------------------------------|
| `Dockerfile`                                | Imagen del backend                         |
| `compose.yml`                               | Servicios `database` + `app`, red, volumen |
| `.dockerignore`                             | Contexto de build                          |
| `.env.example`                              | Plantilla de variables                     |
| `.env`                                      | Valores locales (no commitear)             |
| `scripts/validate-docker-stack.ps1` / `.sh` | Comprobación rápida del endpoint OpenAPI   |

---

