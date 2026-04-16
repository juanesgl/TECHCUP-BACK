package edu.dosw.proyect.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    private final SwaggerConfig swaggerConfig = new SwaggerConfig();

    @Test
    void customOpenAPI_RetornaOpenAPIConfigurado() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("TechCup Futbol API", openAPI.getInfo().getTitle());
        assertEquals("1.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getTags());
        assertEquals(17, openAPI.getTags().size());
    }

    @Test
    void customOpenAPI_ContactoConfigurado() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI.getInfo().getContact());
        assertEquals("TechCup - Equipo CVDS",
                openAPI.getInfo().getContact().getName());
        assertEquals("techcup@escuelaing.edu.co",
                openAPI.getInfo().getContact().getEmail());
    }

    @Test
    void customOpenAPI_SecuritySchemeConfigurado() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertTrue(openAPI.getComponents().getSecuritySchemes()
                .containsKey("Autenticacion Bearer"));
    }
}