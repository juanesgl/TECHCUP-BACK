# Configuración de Azure Database for PostgreSQL - TechCup

## 1. Configuración de instancia en Azure Database for PostgreSQL

### Paso a paso en el Portal de Azure

1. Ingresar al [Portal de Azure](https://portal.azure.com)
2. Buscar **"Azure Database for PostgreSQL"** en la barra de búsqueda
3. Clic en **"+ Crear"**
4. Seleccionar **"Servidor flexible"** (recomendado)
5. Configurar los datos básicos:

| Campo | QA | PROD |
|---|---|---|
| Suscripción | Tu suscripción de Azure | Tu suscripción de Azure |
| Grupo de recursos | `techcup-rg-qa` | `techcup-rg-prod` |
| Nombre del servidor | `techcup-server-qa` | `techcup-server-prod` |
| Región | East US (o la más cercana) | East US (o la más cercana) |
| Versión PostgreSQL | 15 | 15 |
| Tipo de carga de trabajo | Desarrollo | Producción |
| Nombre de usuario admin | `techcup_admin` | `techcup_admin` |
| Contraseña | *(contraseña segura)* | *(contraseña segura diferente)* |

6. En **Proceso y almacenamiento**, seleccionar el tier según el ambiente:
   - **QA**: `Burstable B1ms` (1 vCPU, 2 GB RAM) — económico para pruebas
   - **PROD**: `General Purpose D2s_v3` (2 vCPU, 8 GB RAM) — rendimiento estable

7. Clic en **"Revisar y crear"** → **"Crear"**

---

## 2. Definición de reglas de Firewall

Una vez creada la instancia, se deben configurar las reglas de red para permitir el acceso:

### Desde el Portal de Azure:

1. Ir al recurso del servidor PostgreSQL creado
2. En el menú lateral, seleccionar **"Redes"** (Networking)
3. Configurar las siguientes reglas:

### Regla 1: Permitir acceso desde GitHub Actions
- Marcar la casilla **"Permitir acceso público desde cualquier servicio de Azure"**
  - Esto permite que GitHub Actions (y otros servicios de Azure) se conecten

### Regla 2: Permitir acceso desde tu IP local (para desarrollo)
- Clic en **"+ Agregar dirección IP del cliente actual"**
  - Esto añade automáticamente tu IP pública actual

### Regla 3 (Opcional): Permitir rango de IPs del team
- Clic en **"+ Agregar regla de firewall"**

| Campo | Valor |
|---|---|
| Nombre de la regla | `team-desarrollo` |
| IP inicial | `X.X.X.0` |
| IP final | `X.X.X.255` |

4. Clic en **"Guardar"**

> **Nota de seguridad**: En producción, se recomienda restringir el acceso lo máximo posible. Idealmente usar **Private Endpoints** o **VNet Integration** en lugar de reglas de firewall públicas.

---

## 3. Cadena de conexión e integración con variables de entorno

La cadena de conexión ya está integrada en el proyecto mediante archivos `.env`:

### Formato de la cadena de conexión de Azure PostgreSQL:
```
jdbc:postgresql://<nombre-servidor>.postgres.database.azure.com:5432/<nombre-db>?sslmode=require
```

### Archivos de configuración:

- **`.env.qa`** — Variables para el ambiente de QA (rama `develop`)
- **`.env.prod`** — Variables para el ambiente de PROD (rama `main`)

### Pasos para configurar la conexión:

1. Obtener los datos de conexión del servidor Azure:
   - Ir al recurso del servidor en Azure Portal
   - En **"Información general"**, copiar el **"Nombre del servidor"**

2. Crear la base de datos `techcup_db` en el servidor Azure:
   ```bash
   # Conectarse al servidor Azure
   psql "host=techcup-server-qa.postgres.database.azure.com port=5432 dbname=postgres user=techcup_admin sslmode=require"

   # Crear la base de datos
   CREATE DATABASE techcup_db;
   ```

3. Actualizar los archivos `.env` con los datos reales:

   **`.env.qa`**:
   ```properties
   SPRING_DATASOURCE_URL=jdbc:postgresql://techcup-server-qa.postgres.database.azure.com:5432/techcup_db?sslmode=require
   SPRING_DATASOURCE_USERNAME=techcup_admin
   SPRING_DATASOURCE_PASSWORD=<tu-contraseña-qa>
   ```

   **`.env.prod`**:
   ```properties
   SPRING_DATASOURCE_URL=jdbc:postgresql://techcup-server-prod.postgres.database.azure.com:5432/techcup_db?sslmode=require
   SPRING_DATASOURCE_USERNAME=techcup_admin
   SPRING_DATASOURCE_PASSWORD=<tu-contraseña-prod>
   ```

### Cómo funciona la integración:

El archivo `application.properties` **no** incluye contraseñas ni secretos JWT en el código: lee obligatoriamente variables de entorno (o un archivo `.env` local vía `spring.config.import`, ver `.env.example` en la raíz del backend).

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
jwt.secret=${JWT_SECRET}
```

- **Azure (QA/PROD):** definir esas variables en el servicio de cómputo (p. ej. Azure App Service) o, mejor, referenciar secretos desde **Azure Key Vault**; JDBC con `sslmode=require` (ver ejemplos arriba en este documento).
- **Local con Docker (stack completo):** `docker-compose.yml` levanta **PostgreSQL** y la **API** en contenedor; Compose inyecta `SPRING_DATASOURCE_URL` hacia el host `database` en la red interna. El archivo `.env` aporta usuario, contraseña, JWT, etc. (sin commitear).
- **Local híbrido (solo Postgres en Docker):** ejecutar la API con Maven/IDE y en `.env` usar `SPRING_DATASOURCE_URL` hacia `localhost` y el puerto publicado (p. ej. `5433`).

---

## 4. Validación de conexión desde QA y PROD

### Validación automática en el Pipeline CI

Cuando exista un workflow en GitHub Actions, un paso típico valida la conexión antes de pruebas (usando **GitHub Secrets**, no archivos `.env` en el repo), por ejemplo:

```yaml
- name: Validate Database Connection
  run: |
    PGPASSWORD="${{ secrets.DB_PASSWORD }}" psql \
      "host=${{ secrets.DB_HOST }} port=5432 dbname=techcup_db sslmode=require" \
      -U "${{ secrets.DB_USER }}" \
      -c "SELECT version();"
```

- Si la conexión es exitosa, el pipeline continúa; si falla, se detiene con un error claro.

### Validación manual desde tu máquina local

```bash
# Validar conexión QA
psql "host=techcup-server-qa.postgres.database.azure.com port=5432 dbname=techcup_db user=techcup_admin sslmode=require"

# Validar conexión PROD
psql "host=techcup-server-prod.postgres.database.azure.com port=5432 dbname=techcup_db user=techcup_admin sslmode=require"
```

Si la conexión es exitosa, verás el prompt de PostgreSQL (`techcup_db=>`).

---

## Resumen de archivos relevantes

| Archivo | Rol |
|---|---|
| `application.properties` | Lee BD y JWT solo desde variables de entorno (sin secretos en el código) |
| `.env.example` | Plantilla de variables para local y Docker (copiar a `.env`, no commitear) |
| `docker-compose.yml` | Orquesta Postgres + API; credenciales vía `.env` |
| `Dockerfile` | Imagen del backend Spring Boot (Java 21) |
| GitHub Secrets (cuando haya CI) | Credenciales QA/PROD para pipelines, no en el repositorio |
