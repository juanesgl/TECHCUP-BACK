# Sprint 3

##  Integrantes del Equipo

| Nombre                      | Rol      |
|-----------------------------|----------|
| Isaac David Burgos          | Líder    |
| Andrea Mariana Parra Urrego | Frontend |
| Juan Esteban Sanchez        | Backend  |
| Laura Valentina Santiago    | Backend  |
| Zharik Natalia Mahecha      | Backend  |

### Objetivo del Sprint
Elevar el sistema TechCup a un nivel de producción real implementando las funcionalidades core del torneo: configuración de torneos, gestión y consulta de alineaciones, consulta de partidos, tabla de posiciones automática y estadísticas. Adicionalmente, migrar la persistencia de memoria a base de datos relacional, blindar la API con seguridad 
### Historias incluidas

- TC-14 Configurar torneo
- TC-15 Gestión de alineaciones del team
- TC-16 Consultar alineación rival
- TC-18 Consultar partidos
- TC-19 Tabla de posiciones automática
- TC-21 Estadísticas del torneo
- Implementación del Diagrama de Entidad-Relación
-  Persistencia 
- Seguridad 

#### Swagger

![Swagger pantalla](evidencias/swaggerPartidos.png)

En la imagen podemos observar los endpoints que creamos para cada Tag, donde los endpoints son los siguientes

POST      /api/partidos/consultar   Consultar partidos
GET       /api/partidos/{partidoId} Consultar detalle de un partido


#### Postman

- Prueba 1 Cosultar todos los partidos

Se consultan todos los partidos programados sin aplicar ningun tipo de filtro

![Prueba 1](evidencias/postman1_partidos.png)

- Prueba 2 Filtramos por soccerField

Se aplica el filtro por soccerField para solo observar los partidos de una soccerField especifica

![Prueba 2](evidencias/postman2_partidos.png)

- Prueba 3 Filtro por teams

Se aplica el filtro por teams para solo obtener lso partidos de un team en especifico.

![Prueba 3](evidencias/postman3_partidos.png)

- Prueba 4 Filtro por torneo

Se aplica el filtro por el ID  del torneo, permitiendo ver los partidos de un torneo en especifico.

![Preueba 4](evidencias/postman4_partidos.png)

- Prueba 5 Consultar un partido en especifico

Se consulta el detalle completo de un partido por su ID.

![Prueba 5](evidencias/postman5_partido.png)

- Prueba 6 Filtro sin resultados

Se aplica un fitro con una soccerField que no existe

![Prueba 6](evidencias/postman6_partidos.png)


### Diagrama entidad-relacion


```mermaid
erDiagram
    USUARIO {
        Bigint id PK
        Varchar correo
        Varchar contrasena_hash
        Varchar tipo_usuario
        Varchar nombre
        Varchar apellido
        timestamp fecha_registro
        boolean activo
    }
    JUGADOR {
        Bigint id PK
        Bigint usuario_id FK
        Varchar foto_url
        Varchar posiciones
        int dorsal
        boolean disponible
        Varchar semestre
        int edad
        Varchar genero
        Varchar identificacion
    }
    EQUIPO {
        Bigint id PK
        Varchar nombre
        Varchar escudo_url
        Varchar color_uniforme_local
        Varchar color_uniforme_visita
        Biginit capitan_id FK
        Biginit torneo_id FK
        Varchar estado_inscripcion
    }
    EQUIPO_JUGADOR {
        Bigint id PK
        Bigint equipo_id FK
        Bigint jugador_id FK
        timestamp fecha_union
        boolean activo
    }
    INVITACION {
        Bigint id PK
        Bigint equipo_id FK
        Bigint jugador_id FK
        Varchar estado
        timestamp fecha_envio
        timestamp fecha_respuesta
    }
    TORNEO {
        Bigint id PK
        Varchar nombre
        date fecha_inicio
        date fecha_fin
        int cantidad_equipos
        decimal costo_por_equipo
        string estado
        Bigint organizador_id FK
    }
    CONFIGURACION_TORNEO {
        Bigint id PK
        Biginit torneo_id FK
        text reglamento
        date cierre_inscripciones
        text sanciones
        Varchar fechas_importantes
    }
    CANCHA {
        Bigint id PK
        Varchar nombre
        Varchar direccion
        Varchar descripcion
    }
    PAGO {
        Bigint id PK
        Bigint equipo_id FK
        Bigint torneo_id FK
        Varchar comprobante_url
        Varchar estado
        timestamp fecha_subida
        timestamp fecha_revision
        Bigint revisado_por FK
    }
    PARTIDO {
        Bigint id PK
        Bigint torneo_id FK
        Bigint equipo_local_id FK
        Bigint equipo_visitante_id FK
        Bigint cancha_id FK
        Bigint arbitro_id FK
        datetime fecha_hora
        int goles_local
        int goles_visitante
        Varchar fase
        Varchar estado
    }
    EVENTO_PARTIDO {
        Bigint id PK
        Bigint partido_id FK
        Bigint jugador_id FK
        Varchar tipo_evento
        int minuto
        Varchar descripcion
    }
    ALINEACION {
        Bigint id PK
        Bigint partido_id FK
        Bigint equipo_id FK
        Varchar formacion
        timestamp fecha_registro
    }
    ALINEACION_JUGADOR {
        Bigint id PK
        Bigint alineacion_id FK
        Bigint jugador_id FK
        Varchar rol
        Varchar posicion_en_cancha
        int numero_camiseta
    }
    LLAVE_ELIMINATORIA {
        Bigint id PK
        Bigint torneo_id FK
        Varchar fase
        int numero_llave
        Bigint equipo1_id FK
        Bigint equipo2_id FK
        Bigint partido_id FK
        Bigint ganador_id FK
    }
    ESTADISTICA_EQUIPO {
        Bigint id PK
        Bigint equipo_id FK
        Bigint torneo_id FK
        int partidos_jugados
        int partidos_ganados
        int partidos_empatados
        int partidos_perdidos
        int goles_favor
        int goles_contra
        int diferencia_gol
        int puntos
    }
    USUARIO ||--o| JUGADOR : tiene
    JUGADOR ||--o{ EQUIPO_JUGADOR : pertenece
    EQUIPO ||--o{ EQUIPO_JUGADOR : tiene
    EQUIPO }o--|| JUGADOR : capitan
    EQUIPO }o--|| TORNEO : participa
    JUGADOR ||--o{ INVITACION : recibe
    EQUIPO ||--o{ INVITACION : envia
    TORNEO ||--o| CONFIGURACION_TORNEO : config
    TORNEO }o--|| USUARIO : organiza
    EQUIPO ||--o{ PAGO : paga
    PAGO }o--|| TORNEO : corresponde
    PAGO }o--o| USUARIO : revisa
    PARTIDO }o--|| TORNEO : pertenece
    PARTIDO }o--|| EQUIPO : local
    PARTIDO }o--|| EQUIPO : visitante
    PARTIDO }o--o| CANCHA : juega
    PARTIDO }o--o| USUARIO : arbitro
    PARTIDO ||--o{ EVENTO_PARTIDO : tiene
    EVENTO_PARTIDO }o--|| JUGADOR : realiza
    PARTIDO ||--o{ ALINEACION : tiene
    ALINEACION }o--|| EQUIPO : pertenece
    ALINEACION ||--o{ ALINEACION_JUGADOR : incluye
    ALINEACION_JUGADOR }o--|| JUGADOR : asigna
    TORNEO ||--o{ LLAVE_ELIMINATORIA : genera
    LLAVE_ELIMINATORIA }o--o| EQUIPO : eq1
    LLAVE_ELIMINATORIA }o--o| EQUIPO : eq2
    LLAVE_ELIMINATORIA }o--o| PARTIDO : partido
    LLAVE_ELIMINATORIA }o--o| EQUIPO : ganador
    TORNEO ||--o{ ESTADISTICA_EQUIPO : tiene
    ESTADISTICA_EQUIPO }o--|| EQUIPO : pertenece

```



- El diagrama de entidad-relación muestra el modelo de datos del sistema TechCup, es decir, cómo están estructuradas las tablas, qué atributos tiene cada una y cómo se relacionan entre ellas a través de llaves foráneas.
Comenzando por el centro del diagrama, la entidad más importante es PARTIDO, que actúa como el núcleo del sistema porque casi todas las demás entidades se conectan a ella. Un partido tiene su propio id como llave primaria, referencias al torneo al que pertenece (torneo_id), al team local (equipo_local_id), al team visitante (equipo_visitante_id), y también guarda la soccerField, la fecha, el estado y los goles de cada team.
- La entidad EQUIPO está en la parte superior y es también muy central. Guarda el nombre, escudo, colores del uniforme, y tiene referencias al capitán y a los jugadores del team. Un team puede participar en muchos partidos, tanto como local como visitante, por eso tiene dos relaciones distintas hacia PARTIDO.
- La entidad TORNEO se relaciona con PARTIDO con una cardinalidad de uno a muchos, es decir, un torneo puede tener muchos partidos. También se relaciona con CONFIGURACION_TORNEO, que guarda parámetros como el reglamento y fechas límite.
- La entidad JUGADOR está en la parte inferior izquierda y guarda información del jugador como su contraseña hash, tipo de posición, cuota, goles y si está disponible. Se relaciona con RELACION_JUGADOR para gestionar su participación en teams, y con ALINEACION_JUGADOR para registrar en qué alineaciones ha participado.
- La entidad ESTADISTICA_EQUIPO recoge los datos calculados de cada team por torneo, incluyendo partidos jugados, partidos ganados, empates, goles a favor, goles en contra, diferencia de goles y puntos. Esta entidad se actualiza automáticamente cuando se registra el resultado de un partido.
- La entidad TEAM_LINEUP o alineación del team está en el centro superior y guarda el torneo_id, equipo_id, partido_id, capitan_id, la formación táctica seleccionada y el estado de la alineación. Se relaciona con ALINEACION_JUGADOR, que es la tabla intermedia que asocia cada jugador con su posición dentro de esa alineación específica.
- La entidad CANCHA guarda información de cada soccerField donde se juegan los partidos, incluyendo el nombre, la dirección y su disponibilidad.
- La entidad PAGO gestiona los comprobantes de pago de inscripción al torneo, relacionando un jugador con un torneo específico y guardando la fecha, el estado y la descripción del pago.
- La entidad ALINEACION_TITULAR junto con ALINEACION_JUGADOR implementan la lógica de que exactamente 7 jugadores deben ser titulares por partido, con sus posiciones asignadas según la formación táctica elegida.