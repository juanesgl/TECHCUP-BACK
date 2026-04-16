package edu.dosw.proyect.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TechCup Fútbol API")
                        .version("1.0")
                        .description("""
                                API REST para la gestión del torneo semestral de fútbol
                                de los programas de Ingeniería de Sistemas, IA,
                                Ciberseguridad y Estadística — Escuela Colombiana
                                de Ingeniería Julio Garavito.

                                **Autenticación:** Usa el botón Authorize e ingresa:
                                Bearer {token}

                                **Login clásico:** POST /api/users/login

                                **Login con Google:** GET /oauth2/authorization/google
                                (solo para familiares, organizadores y árbitros)
                                """)
                        .contact(new Contact()
                                .name("TechCup — Equipo CVDS")
                                .email("techcup@escuelaing.edu.co")))

                .addSecurityItem(new SecurityRequirement().addList("Autenticacion Bearer"))
                .components(new Components()
                        .addSecuritySchemes("Autenticacion Bearer",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))

                .tags(List.of(
                        new Tag().name("01 Autenticacion")
                                .description("Login con email y contraseña. Retorna JWT."),
                        new Tag().name("02 Registro de Usuarios")
                                .description("""
                                        Registro de estudiantes, profesores, graduados,
                                        familiares, organizadores y árbitros.
                                        Un usuario puede consultar y actualizar su propio perfil.
                                        """),
                        new Tag().name("03 Organizador Torneos")
                                .description("""
                                        Crear, iniciar y finalizar torneos.
                                        Generar y consultar el bracket eliminatorio
                                        (cuartos de final, semifinales y final).
                                        """),
                        new Tag().name("04 Organizador Configuracion")
                                .description("""
                                        Configurar canchas, fecha de cierre de inscripciones,
                                        reglamento, horarios y sanciones del torneo.
                                        """),
                        new Tag().name("05 Organizador Pagos")
                                .description("""
                                        Aprobar o rechazar comprobantes de pago Nequi.
                                        Monto único: $130.000 COP por equipo.
                                        """),
                        new Tag().name("06 Organizador Estadisticas")
                                .description("""
                                        Consultar tabla de posiciones, goleadores
                                        y estadísticas generales del torneo.
                                        """),
                        new Tag().name("07 Arbitro Partidos")
                                .description("""
                                        Consultar partidos asignados: fecha, hora,
                                        cancha y equipos participantes.
                                        """),
                        new Tag().name("08 Arbitro Resultados")
                                .description("""
                                        Registrar resultado completo de un partido:
                                        marcador, goleadores, tarjetas amarillas
                                        y tarjetas rojas.
                                        """),
                        new Tag().name("09 Capitan Equipos")
                                .description("""
                                        Crear y gestionar el equipo.
                                        Reglas: mínimo 8, máximo 12 integrantes.
                                        Más de la mitad deben ser del programa de Sistemas.
                                        """),
                        new Tag().name("10 Capitan Invitaciones")
                                .description("""
                                        Las invitaciones se envían automáticamente
                                        al crear el equipo.
                                        """),
                        new Tag().name("11 Capitan Alineaciones")
                                .description("""
                                        Guardar y actualizar la alineación táctica.
                                        Seleccionar 7 titulares, formación y reservas (TC-15).
                                        """),
                        new Tag().name("12 Capitan Pagos")
                                .description("""
                                        Subir comprobante de pago Nequi ($130.000 COP).
                                        Solo se acepta pago único antes de la fecha límite.
                                        """),
                        new Tag().name("13 Jugador Disponibilidad")
                                .description("""
                                        Activar o desactivar disponibilidad para
                                        recibir invitaciones de equipos.
                                        """),
                        new Tag().name("14 Jugador Invitaciones")
                                .description("""
                                        Aceptar o rechazar invitaciones de equipos.
                                        Al aceptar, las demás invitaciones se rechazan
                                        automáticamente.
                                        """),
                        new Tag().name("15 Jugador Partidos")
                                .description("""
                                        Consultar partidos programados con filtros
                                        por fecha, cancha, equipo o torneo (TC-18).
                                        """),
                        new Tag().name("16 Jugador Alineacion Rival")
                                .description("""
                                        Consultar la alineación del equipo rival
                                        para un partido programado (TC-16).
                                        """),
                        new Tag().name("17 Jugador Busqueda")
                                .description("""
                                        Buscar jugadores disponibles por nombre,
                                        posición, edad o semestre.
                                        """)
                ));
    }
}