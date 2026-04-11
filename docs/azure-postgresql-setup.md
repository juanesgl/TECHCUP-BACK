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

### Regla 3 (Opcional): Permitir rango de IPs del equipo
- Clic en **"+ Agregar regla de firewall"**

| Campo | Valor |
|---|---|
| Nombre de la regla | `equipo-desarrollo` |
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

El archivo `application.properties` usa la sintaxis `${VARIABLE:valor_por_defecto}` de Spring Boot:
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/techcup_db}
```
- Si la variable de entorno `SPRING_DATASOURCE_URL` **existe** → usa su valor (Azure)
- Si la variable de entorno **no existe** → usa el valor por defecto (`localhost`, para desarrollo local)

---

## 4. Validación de conexión desde QA y PROD

### Validación automática en el Pipeline CI

Se añadió un paso en el workflow de GitHub Actions (`.github/workflows/ci.yml`) que valida la conexión antes de ejecutar las pruebas:

```yaml
- name: Validate Database Connection
  run: |
    PGPASSWORD="${{ env.SPRING_DATASOURCE_PASSWORD }}" psql \
      "${{ env.SPRING_DATASOURCE_URL }}" \
      -U "${{ env.SPRING_DATASOURCE_USERNAME }}" \
      -c "SELECT version();"
```

- ✅ Si la conexión es exitosa, el pipeline continúa
- ❌ Si falla, el pipeline se detiene inmediatamente con un error claro

### Validación manual desde tu máquina local

```bash
# Validar conexión QA
psql "host=techcup-server-qa.postgres.database.azure.com port=5432 dbname=techcup_db user=techcup_admin sslmode=require"

# Validar conexión PROD
psql "host=techcup-server-prod.postgres.database.azure.com port=5432 dbname=techcup_db user=techcup_admin sslmode=require"
```

Si la conexión es exitosa, verás el prompt de PostgreSQL (`techcup_db=>`).

---

## Resumen de archivos modificados

| Archivo | Cambio |
|---|---|
| `application.properties` | Usa `${VAR:default}` para leer credenciales de variables de entorno |
| `.env.qa` | Contiene credenciales de Azure PostgreSQL para QA |
| `.env.prod` | Contiene credenciales de Azure PostgreSQL para PROD |
| `.github/workflows/ci.yml` | Carga `.env` según rama y valida conexión a BD |
