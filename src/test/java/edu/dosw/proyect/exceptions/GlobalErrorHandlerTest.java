package edu.dosw.proyect.exceptions;

import edu.dosw.proyect.core.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalErrorHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBusinessRuleException_RetornaConflict() {
        BusinessRuleException ex = new BusinessRuleException("Regla violada");

        ResponseEntity<Map<String, Object>> response = handler.handleBusinessRule(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflicto de regla de negocio", response.getBody().get("error"));
        assertEquals("Regla violada", response.getBody().get("message"));
    }

    @Test
    void handleResourceNotFoundException_RetornaNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("No encontrado");

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Recurso no encontrado", response.getBody().get("error"));
        assertEquals("No encontrado", response.getBody().get("message"));
    }

    @Test
    void handleDisponibilidadException_RetornaConflict() {
        DisponibilidadException ex = new DisponibilidadException("Conflicto disponibilidad");

        ResponseEntity<Map<String, Object>> response = handler.handleDisponibilidad(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleGeneralException_RetornaInternalServerError() {
        Exception ex = new Exception("Error generico");

        ResponseEntity<Map<String, Object>> response = handler.handleGeneral(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(String.valueOf(response.getBody().get("error")).contains("Error interno"));
    }
}