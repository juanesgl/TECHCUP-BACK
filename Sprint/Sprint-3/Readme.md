# Sprint 3

##  Integrantes del Equipo

| Nombre | Rol     |
|--------|---------|
| Juan Esteban Sanchez | Backend |
| Zharik Natalia Mahecha | Backend |
| Mariana Parra Urrego | Fronted |
| Isaac David Burgos | Lider   |
| Laura Valentina Santiago | Backend |


### Consultar partido

#### Swagger

![Swagger pantalla](swaggerPartidos.png)

En la imagen podemos observar los endpoints que creamos para cada Tag, donde los endpoints son los siguientes

POST      /api/partidos/consultar   Consultar partidos
GET       /api/partidos/{partidoId} Consultar detalle de un partido


#### Postman

- Prueba 1 Cosultar todos los partidos

Se consultan todos los partidos programados sin aplicar ningun tipo de filtro

![Prueba 1](postman1_partidos.png)

- Prueba 2 Filtramos por cancha

Se aplica el filtro por cancha para solo observar los partidos de una cancha especifica

![Prueba 2](postman2_partidos.png)

- Prueba 3 Filtro por equipos

Se aplica el filtro por equipos para solo obtener lso partidos de un equipo en especifico.

![Prueba 3](postman3_partidos.png)

- Prueba 4 Filtro por torneo

Se aplica el filtro por el ID  del torneo, permitiendo ver los partidos de un torneo en especifico.

![Preueba 4](postman4_partidos.png)

- Prueba 5 Consultar un partido en especifico

Se consulta el detalle completo de un partido por su ID.

![Prueba 5](postman5_partido.png)

- Prueba 6 Filtro sin resultados

Se aplica un fitro con una cancha que no existe

![Prueba 6](postman6_partidos.png)


### Diagrama entidad-relacion

![Diagrama de entidad-relacion](../../docs/uml/diagrama_entidad_relacion.png)

- El diagrama de entidad-relación muestra el modelo de datos del sistema TechCup, es decir, cómo están estructuradas las tablas, qué atributos tiene cada una y cómo se relacionan entre ellas a través de llaves foráneas.
Comenzando por el centro del diagrama, la entidad más importante es PARTIDO, que actúa como el núcleo del sistema porque casi todas las demás entidades se conectan a ella. Un partido tiene su propio id como llave primaria, referencias al torneo al que pertenece (torneo_id), al equipo local (equipo_local_id), al equipo visitante (equipo_visitante_id), y también guarda la cancha, la fecha, el estado y los goles de cada equipo.
- La entidad EQUIPO está en la parte superior y es también muy central. Guarda el nombre, escudo, colores del uniforme, y tiene referencias al capitán y a los jugadores del equipo. Un equipo puede participar en muchos partidos, tanto como local como visitante, por eso tiene dos relaciones distintas hacia PARTIDO.
- La entidad TORNEO se relaciona con PARTIDO con una cardinalidad de uno a muchos, es decir, un torneo puede tener muchos partidos. También se relaciona con CONFIGURACION_TORNEO, que guarda parámetros como el reglamento y fechas límite.
- La entidad JUGADOR está en la parte inferior izquierda y guarda información del jugador como su contraseña hash, tipo de posición, cuota, goles y si está disponible. Se relaciona con RELACION_JUGADOR para gestionar su participación en equipos, y con ALINEACION_JUGADOR para registrar en qué alineaciones ha participado.
- La entidad ESTADISTICA_EQUIPO recoge los datos calculados de cada equipo por torneo, incluyendo partidos jugados, partidos ganados, empates, goles a favor, goles en contra, diferencia de goles y puntos. Esta entidad se actualiza automáticamente cuando se registra el resultado de un partido.
- La entidad TEAM_LINEUP o alineación del equipo está en el centro superior y guarda el torneo_id, equipo_id, partido_id, capitan_id, la formación táctica seleccionada y el estado de la alineación. Se relaciona con ALINEACION_JUGADOR, que es la tabla intermedia que asocia cada jugador con su posición dentro de esa alineación específica.
- La entidad CANCHA guarda información de cada cancha donde se juegan los partidos, incluyendo el nombre, la dirección y su disponibilidad.
- La entidad PAGO gestiona los comprobantes de pago de inscripción al torneo, relacionando un jugador con un torneo específico y guardando la fecha, el estado y la descripción del pago.
- La entidad ALINEACION_TITULAR junto con ALINEACION_JUGADOR implementan la lógica de que exactamente 7 jugadores deben ser titulares por partido, con sus posiciones asignadas según la formación táctica elegida.