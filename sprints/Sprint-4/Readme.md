# Sprint 4

##  Integrantes del Equipo

| Nombre                      | Rol     |
|-----------------------------|---------|
| Isaac David Burgos          | Backend |
| Andrea Mariana Parra Urrego | Backend |
| Juan Esteban Sanchez        | Backend |
| Laura Valentina Santiago    | Fronted |
| Zharik Natalia Mahecha      | Lider   |


### Objetivo del Sprint
Resolver la deuda técnica del Sprint #3, avanzar en la implementación del pipeline CI/CD con GitHub Actions y Azure, e iniciar la implementación de pantallas frontend con conectividad al backend mediante Axios.


### Historias incluidas

- TC - 17: Registro de partidos
- TC - 20: llaves eliminatorias

### Diagrama clases actualizado

![DiagramaClases.png](evidencias/DiagramaClases.png)

El diagrama representa el modelo de dominio del sistema, donde se definen las entidades principales y sus relaciones para gestionar torneos de fútbol, teams, jugadores y partidos.”

### Diagrama De Despliegue

![Diagrama.png](../../docs/uml/Diagrama.png)

el diagrama muestra el camino que sigue el código desde la PC del programador hasta que llega al usuario final en la nube:
Local: El desarrollador escribe el código en Spring Boot 3 y lo prueba con una base de datos local en Docker.
GitHub (CI/CD): Al subir el código (git push), GitHub Actions toma el relevo. Compila el proyecto con Maven, analiza la calidad con SonarCloud y genera el archivo ejecutable (.jar).
Azure: Si el código es de la rama de desarrollo, se despliega en el entorno de QA; si es la rama principal, se va directo a Producción. Ambos usan bases de datos PostgreSQL hospedadas en Azure.
Es un flujo automatizado para asegurar que nada se rompa antes de publicar.