# SPRINT 1

##  Integrantes del Equipo

| Nombre                   | Rol      |
|--------------------------|----------|
| Laura Valentina Santiago | Lider    |
| Juan Esteban Sanchez     | Backend  |
| Zharik Natalia Mahecha   | Backend  |
| Mariana Parra Urrego     | Frontend |
| Isaac David Burgos       | Frontend |



## Descripción

Este repositorio contiene la implementación del backend del sistema TechCup,
una plataforma diseñada para la gestión de torneos de fútbol universitarios.

El sistema está construido utilizando Spring Boot y sigue una arquitectura
MVC para organizar los componentes del sistema.

## Arquitectura

El backend sigue el patrón MVC:

Controller → Maneja las peticiones HTTP
Service → Implementa la lógica de negocio
Models / DTO → Representan la información del sistema
Validators → Validan los datos de entrada

## Deuda Técnica

#### Requerimientos

![Requerimientos arreglados](Sprint/Sprint-1/evidencias/pruebas/requerimientos_arreglados.png)

#### Manual de identidad

[Manual de identidad TECHCUP](docs/requeriments/Manual%20de%20identidad%20TECHCUP.pdf)

#### Diagrama de contexto

![Diagrama de contexto](docs/uml/diagrama_de_contexto.png)

- En el centro está TechCup, definida como una plataforma digital para la gestión del torneo semestral de fútbol. Todo lo que ocurre dentro de esa elipse es el sistema que estamos construyendo, y todo lo que está afuera son actores externos con los que el sistema se comunica.
- Alrededor del sistema identificamos seis actores externos:
- El Jugador es el actor principal. Se inscribe en el torneo, gestiona su perfil deportivo y recibe información del torneo como partidos y resultados. También se conecta con Nequi para realizar el pago de inscripción y generar el comprobante.
- Nequi es el único sistema externo del diagrama, por eso aparece en rojo diferenciándose de los actores humanos. Funciona como la pasarela de pago: el jugador le envía el dinero y Nequi genera el comprobante que luego se sube a TechCup para ser aprobado.
- El Capitán gestiona todo lo relacionado con su equipo: armar el equipo, gestionar pagos y configurar las alineaciones para cada partido. También recibe del sistema la programación de los partidos en los que participa.
- El Organizador administra el torneo en general, aprueba los pagos de inscripción de los equipos y gestiona las llaves eliminatorias junto con el sistema.
- El Administrador es el actor con mayor nivel de acceso y gestiona el sistema de forma completa, incluyendo configuraciones globales, usuarios y torneos.
- El Árbitro tiene la interacción más limitada, únicamente consulta la información de los partidos que tiene asignados, como fecha, cancha y equipos participantes.
- Este diagrama nos permite entender de un solo vistazo quiénes usan TechCup y para qué, sin necesidad de conocer nada técnico del sistema. Define claramente los límites de lo que el equipo de desarrollo debe construir y las integraciones externas que debe contemplar, en este caso únicamente Nequi como sistema de pago

#### Jira

[Ver tablero en Jira](https://juanspacee.atlassian.net/jira/software/projects/TEC/boards/166/timeline)

#### Mockups y flujos de navegación corregidos

[Ver Mockups en Figma](https://www.figma.com/design/vK0m1sGamwU5v0DbajWtIZ/Academic-Sports-Dashboard-UI-in-Figma--Web-Design-Tutorial--Community-?node-id=0-1)
## Requerimientos funcionales

TC-01  Iniciar sesión  
TC-02  Crear torneo  
TC-03  Inicializar torneo  
TC-04  Finalizar torneo  
TC-05  Consultar torneos  
TC-06  Registrar jugadores  
TC-07  Crear perfil deportivo

## Gestión del proyecto

El proyecto se organiza utilizando metodología ágil Scrum.

Cada materia se gestiona como una épica dentro de Jira y las funcionalidades se
organizan mediante Features, Historias de usuario y Subtareas.

Se utiliza planificación por sprints para dividir el desarrollo en iteraciones.

## Sprint 1

### Objetivo del Sprint
Implementar las funcionalidades iniciales del sistema relacionadas con la autenticación
de usuarios y la gestión básica de torneos.

### Historias incluidas

- Iniciar sesión
- Crear torneo
- Inicializar torneo
- Finalizar torneo

## Matriz de trazabilidad

La matriz de trazabilidad permite relacionar los requerimientos funcionales con las
historias de usuario, tareas y funcionalidades implementadas en el sistema.

[Matriz de trazabilidad](docs/requeriments/MatrizDeTrazabilidad.pdf)

## Patrones de diseño utilizados

1. Factory Method: para crear los diferentes tipos de usuario.
   El sistema necesita crear muchas tipos de usuario distintos: estudiantrs, graduados, profesore.., poner todo eso en un solo bloque de codigo seria dficil de mantener porque tendria muchos if- else.
   con factory method cada usuario tiene su propia fabica que sabe como crearlo, si despues necesito agregar un nuevo tipo de usuario, solo creo una nueva fabrica sin tocar lo que ya existe
2. Strategy: para la autentificacion
   Para ingresar al sistema hay dos formas con correo institucional o Gmail, en lugar de llenar el login con condiciones es mas facil encapsular cada forma de identificarse en su propia clase
3. Builder: Para crear torneos
   Crear un torneo no es solo darle un nombre, tiene fechas, canchas, horarios...
   builder ayuda a crear el torneo paso a paso
4. Chain of responsability: para validar
   cuando el capitan sube el comprobante de pago, ese archivo debe pasar por varias validaciones, con este patron cada validador revisa lo suyyo y si pasa lo manda al siguiente, en caso de fallas se detiene y retona el rechazo
5. Observer: Para las notificaciones
   Hay momentos en el sistema donde algo pasa y varias partes necesitan enterarse, en lugar de que cada clase llame a quien necesita saber es mas eficiente que observer les avise automaticamente a los que le interesa esa notificacion.


## Diagramas

### Diagrama de clases
![Diagrama de clases](docs/uml/diagrama_de_clases.png)
- El núcleo del sistema es la clase User, que es una clase abstracta de la cual heredan todos los tipos de usuario del sistema: Student, Graduate, Professor, Referee, Admin, Organizer y FamilyMember. Esta herencia se representa con las flechas que apuntan hacia User desde cada subclase.
- La clase TournamentModel representa el torneo y se relaciona con Match que representa cada partido, y con TournamentManagement que gestiona las operaciones sobre el torneo como crearlo, iniciarlo y finalizarlo.
- La clase Invitation gestiona las invitaciones que el capitán envía a los jugadores para unirse a su equipo, y Notification se encarga de avisar a los usuarios cuando ocurre algo relevante en el sistema.
- En la parte inferior del diagrama se pueden ver las clases relacionadas con los patrones de diseño que implementamos:
- Con Factory Method se crean los diferentes tipos de usuario. Cada tipo tiene su propia fábrica: StudentCreator, GraduateCreator, ProfessorCreator, AdminCreator, RefereeCreator y GraduateCreator, todas heredando de una interfaz común UserCreator. Esto permite agregar nuevos tipos de usuario sin modificar el código existente.
- Con Strategy se maneja la autenticación. Hay dos estrategias concretas: InstitutionalEmailStrategy para usuarios internos con correo institucional, y GmailOAuthStrategy para usuarios externos con Google, ambas implementando la interfaz AuthStrategy.
- Con Builder se construye el torneo paso a paso a través de TournamentBuilder, permitiendo configurar fechas, canchas, costos y reglamento de forma ordenada.
- Con Chain of Responsibility se validan los comprobantes de pago. Hay una cadena de validadores donde cada uno revisa su parte y pasa al siguiente: FileValidator, PaymentValidator, FormatValidator y BusinessValidator, todos heredando de BaseValidator.
- Con Observer se gestionan las notificaciones. La interfaz Observer tiene un método update() que implementan NotificationService y AuditService, y cuando ocurre un evento en el sistema como un pago aprobado o un partido programado, el Subject notifica automáticamente a todos los observadores registrados.

### Diagrama de componentes general

![Diagrama de componentes general](docs/uml/diagrama_componentes_general.png)
- En nuestro diagrama se pueden identificar cuatro elementos principales. El primero es el User, que representa cualquier persona que interactúa con el sistema, ya sea un capitán, un jugador, un organizador o un administrador. Este usuario se conecta al componente de Frontend a través de una interfaz, lo que significa que toda interacción comienza desde la interfaz visual.
El Frontend a su vez se comunica con el Backend mediante una interfaz de tipo puerto, que en nuestro caso corresponde a los endpoints REST documentados con Swagger. Esta comunicación es bidireccional: el frontend envía peticiones y el backend responde con los datos procesados.
El Backend tiene dos dependencias externas. La primera es la Base de Datos, hacia donde apunta con una flecha de dependencia, indicando que el backend persiste y consulta la información del torneo, equipos, partidos, pagos y alineaciones. La segunda dependencia externa es el servicio de Autenticación Gmail, que se conecta a través de una interfaz, representando la integración con OAuth2 de Google que está planificada implementar para los usuarios externos del sistema.
### Diagrama de componentes especifico
![Diagrama de componentes especifico](docs/uml/diagrama_componentes_especifico.png)

- El diagrama de componentes específico muestra cómo está organizado internamente el Backend, identificando los módulos principales y cómo se relacionan entre sí.  este diagrama define el propósito de cada componente interno: controladores, servicios, repositorios, mappers y utilidades.
En nuestro diagrama se pueden ver seis flujos principales, uno por cada módulo funcional del sistema, y todos siguen exactamente la misma estructura en capas de izquierda a derecha.
- El primer flujo es el de Autenticación, donde el AuthController recibe la petición del usuario, la pasa al AuthService a través de una interfaz, este la valida con el AuthValidator, luego el AuthMapper transforma los datos y finalmente el AuthRepository persiste o consulta la información. Al final del flujo hay un componente de Util que es compartido por todos los módulos.
- El segundo flujo es el de Torneos, siguiendo la misma lógica: TorneoController → TorneoService → TorneoValidator → TorneoMapper → TorneoRepository → Util. Este módulo gestiona la creación, inicio y finalización de torneos.
- El tercer flujo corresponde a Jugadores, donde JugadoresController coordina con JugadoresService para gestionar la disponibilidad de los jugadores en la agencia libre, validando con JugadoresValidator antes de persistir en JugadoresRepository.
- El cuarto flujo es el de Equipos, que es uno de los más importantes porque gestiona la creación de equipos, las invitaciones y las alineaciones. El EquipoController delega en EquipoService, que aplica las reglas de negocio TH-01, TH-02 y TH-03 antes de guardar en EquipoRepository.
- El quinto flujo es el de Partidos, donde PartidoController coordina con PartidoService para registrar resultados y actualizar automáticamente la tabla de posiciones, usando PartidoMapper para transformar los datos entre capas.
- El sexto y último flujo es el de Pagos, donde PagoController gestiona la subida de comprobantes y el cambio de estado de los pagos a través de PagoService y PagoRepository.


## Pruebas

Para cada funcionalidad se definieron casos de prueba:

- Casos de éxito
- Casos de error
- Casos condicionales según validaciones

## Evidencia de dailys

Daily 1

¿Qué se realizó?: Leimos el enunciado para esta semana y divimos el trabajo teniendo en cuenta la carga de cada parte.
En el backend teniamos que realizar muchas cosas por lo que asigamos a tres personas para ello.

Bloqueo: ninguno

![Reunion 1](Sprint/Sprint-1/evidencias/daily/Reunion1.png)
![Reunion 1](Sprint/Sprint-1/evidencias/daily/Reunion1.1.png)

Daily 2

¿Qué se realizó?: Revisamos que las correcciones realizadas en la clase anterior por el profesor fueran resueltas para poder comenzar con
la siguiente parte que correspondia al sprint 1. Revisamos dudas que teniamos sobre el enunciado y demas.

Bloqueo: ninguno

![Reunion 2](Sprint/Sprint-1/evidencias/daily/Reunion2.png)

Daily 3

¿Qué se realizó?: Seleccionamos los requerimientos que ibamos a implemnetar en este sprint y realizamos el planning poker.

Bloqueo: Diagramas de clases

![Reunion 3](Sprint/Sprint-1/evidencias/daily/Reunion3.png)

Daily 4

¿Qué se realizó?: Revisamos los avances que teniamos hasta el momento, diagrama de clases teniendo en cuenta los patrones que
habiamos identificado, un poco de la implementacion del codigo y funcionalidad del mockup.

Bloqueo: Dudas en codigo y diagramas de componentes.

![Reunion 4](Sprint/Sprint-1/evidencias/daily/Reunion4.png)

Daily 5

¿Qué se realizó?: Revision de todas las tares que hizo falta y que cosas ya estaban completadas. Organizacion para presentacion

Bloqueo: Ninguno

![Reunion 5](Sprint/Sprint-1/evidencias/daily/Reunion5.png)