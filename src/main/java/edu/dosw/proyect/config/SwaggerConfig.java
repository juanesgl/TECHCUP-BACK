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
                        .title("TechCup Futbol API")
                        .version("1.0")
                        .contact(new Contact()
                                .name("TechCup - Equipo CVDS")
                                .email("techcup@escuelaing.edu.co")))
                .addSecurityItem(new SecurityRequirement().addList("Autenticacion Bearer"))
                .components(new Components()
                        .addSecuritySchemes("Autenticacion Bearer",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .tags(List.of(
                        new Tag().name("Autenticacion")
                                .description("Disponible para todos los usuarios"),
                        new Tag().name("Registro de Usuarios")
                                .description("Disponible para todos"),
                        new Tag().name("Organizador - Torneos")
                                .description("Crear, iniciar y finalizar torneos"),
                        new Tag().name("Organizador - Configuracion")
                                .description("Configurar canchas, fechas y reglamento del torneo"),
                        new Tag().name("Organizador - Pagos")
                                .description("Aprobar o rechazar comprobantes de pago"),
                        new Tag().name("Organizador - Estadisticas")
                                .description("Consultar estadisticas y tabla de posiciones"),
                        new Tag().name("Arbitro - Partidos")
                                .description("Consultar partidos asignados"),
                        new Tag().name("Arbitro - Resultados")
                                .description("Registrar resultados de partidos"),
                        new Tag().name("Capitan - Equipos")
                                .description("Crear y gestionar el equipo"),
                        new Tag().name("Capitan - Invitaciones")
                                .description("Enviar invitaciones a jugadores disponibles"),
                        new Tag().name("Capitan - Alineaciones")
                                .description("Guardar y actualizar alineacion tactica (TC-15)"),
                        new Tag().name("Capitan - Pagos")
                                .description("Subir comprobante de pago del equipo"),
                        new Tag().name("Jugador - Disponibilidad")
                                .description("Activar o desactivar disponibilidad para recibir invitaciones"),
                        new Tag().name("Jugador - Invitaciones")
                                .description("Responder invitaciones de equipos"),
                        new Tag().name("Jugador - Partidos")
                                .description("Consultar partidos programados (TC-18)"),
                        new Tag().name("Jugador - Alineacion Rival")
                                .description("Consultar alineacion del equipo rival (TC-16)"),
                        new Tag().name("Jugador - Busqueda")
                                .description("Buscar jugadores disponibles en el torneo")
                ));
    }
}