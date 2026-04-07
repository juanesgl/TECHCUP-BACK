# Pruebas de Endpoints 1 a 1

## 1) Preparacion

1. Levanta base de datos:
   - docker compose up -d
2. Inicia API:
   - mvn spring-boot:run
  - Para pruebas 1 a 1 sin bloqueo de seguridad: mvn spring-boot:run "-Dspring-boot.run.profiles=localtest"
3. Verifica Swagger:
   - http://localhost:8085/swagger-ui.html
4. Verifica endpoint publico de debug:
   - GET http://localhost:8085/api/debug/users

Nota:
- Actualmente varios endpoints estan protegidos y responden 401/403 sin autenticacion.
- En cada arranque Spring imprime un password temporal para usuario por defecto "user".

## 2) Orden recomendado de pruebas

### 2.1 Endpoints base (publicos)

1. GET /api/debug/users
2. GET /api/debug/users/{email}
3. POST /api/users/register
4. POST /api/users/login

Body de registro sugerido:

{
  "name": "Capitan Demo",
  "email": "capitan.demo@techcup.com",
  "password": "Demo123*",
  "role": "CAPTAIN",
  "preferredPosition": "DEFENDER",
  "skillLevel": 4
}

Body de login sugerido:

{
  "email": "capitan.demo@techcup.com",
  "password": "Demo123*"
}

### 2.2 Torneos

5. POST /api/tournaments
6. GET /api/tournaments
7. PUT /api/tournaments/{id}/start
8. PUT /api/tournaments/{id}/finish
9. PUT /api/v1/torneos/{tournId}/configuracion
10. GET /api/v1/estadisticas/torneo/{tournId}
11. GET /api/tournaments/{tournamentId}/standings

Body de creacion de torneo sugerido:

{
  "name": "Torneo Demo 2026",
  "startDate": "2026-04-10",
  "endDate": "2026-06-20",
  "maxTeams": 16,
  "costPerTeam": 120000,
  "regulation": "Reglamento base demo"
}

Body de configuracion de torneo sugerido:

{
  "registrationCloseDate": "2026-04-01",
  "canchas": [
    {
      "nombre": "Cancha Central",
      "ubicacion": "Campus Norte"
    }
  ],
  "importantDates": "Sorteo 2026-04-05",
  "matchSchedules": "Sabados 8am-4pm",
  "sanctions": "N/A",
  "regulation": "Reglamento actualizado",
  "organizerId": 1
}

### 2.3 Equipos, jugadores, invitaciones

12. POST /api/equipos/crear
13. PUT /api/jugadores/{id}/disponibilidad
14. POST /api/players/filter
15. POST /api/invitaciones/{invitacionId}/responder?jugadorId={jugadorId}

Headers requeridos para crear equipo:
- X-Capitan-ID: {capitanId}

Body crear equipo sugerido:

{
  "nombreEquipo": "Leones FC",
  "escudo": "https://example.com/escudo.png",
  "coloresUniforme": "azul/blanco",
  "jugadoresInvitadosIds": [2, 3, 4, 5, 6, 7]
}

Body disponibilidad sugerido:

{
  "estadoDisponibilidad": true
}

Body filtro jugadores sugerido:

{
  "name": "",
  "position": "DEFENDER",
  "age": 20,
  "semester": "5",
  "available": true
}

Body responder invitacion sugerido:

{
  "respuesta": "ACEPTAR"
}

### 2.4 Partidos, alineaciones y resultados

16. POST /api/partidos/consultar
17. GET /api/partidos/{partidoId}
18. POST /api/lineups
19. PUT /api/lineups/{lineupId}
20. GET /api/lineups/team/{teamId}/match/{matchId}
21. GET /api/lineups/team/{teamId}
22. POST /api/matches/{matchId}/result

Headers para endpoints de lineups:
- X-Captain-ID: {capitanId}

Body filtro partidos sugerido:

{
  "fecha": null,
  "cancha": null,
  "nombreEquipo": null,
  "tournamentId": null
}

Body lineup sugerido:

{
  "teamId": 1,
  "matchId": 1,
  "formation": "F_1_2_3_1",
  "starters": [
    { "playerId": 2, "fieldPosition": "GOALKEEPER" },
    { "playerId": 3, "fieldPosition": "DEFENDER" },
    { "playerId": 4, "fieldPosition": "DEFENDER" },
    { "playerId": 5, "fieldPosition": "MIDFIELDER" },
    { "playerId": 6, "fieldPosition": "MIDFIELDER" },
    { "playerId": 7, "fieldPosition": "FORWARD" },
    { "playerId": 8, "fieldPosition": "FORWARD" }
  ],
  "reserveIds": [9, 10]
}

Body resultado partido sugerido:

{
  "homeGoals": 2,
  "awayGoals": 1
}

### 2.5 Pagos

23. GET /api/payments/test
24. POST /api/payments/upload
25. PUT /api/payments/status

Body upload pago sugerido:

{
  "userId": 1,
  "tournamentId": 1,
  "fileName": "comprobante.png",
  "fileUrl": "https://example.com/comprobante.png"
}

Body actualizar estado pago sugerido:

{
  "paymentId": 1,
  "status": "APPROVED"
}

## 3) Checklist de salida por endpoint

Por cada endpoint guarda:
- Metodo y ruta
- Request enviado
- Status code real
- Response body
- Resultado: OK o FAIL

## 4) Problemas comunes

1. 401 o 403 en endpoints protegidos: falta autenticacion.
2. 404 en endpoints con IDs: el recurso previo no existe aun.
3. 409: regla de negocio valida (estado no permitido).
4. 400: payload invalido o enum incorrecto.
