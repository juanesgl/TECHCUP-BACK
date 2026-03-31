package edu.dosw.proyect.exceptions;

import edu.dosw.proyect.core.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalErrorHandlerTest {

    private final GlobalErrorHandler handler = new GlobalErrorHandler();

    @Test
    void handleBusinessRuleException_RetornaConflict() {
        BusinessRuleException ex = new BusinessRuleException("Regla violada");

        ResponseEntity<Map<String, String>> response =
                handler.handleBusinessRuleException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Regla violada", response.getBody().get("error"));
    }

    @Test
    void handleResourceNotFoundException_RetornaNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("No encontrado");

        ResponseEntity<Map<String, String>> response =
                handler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No encontrado", response.getBody().get("error"));
    }

    @Test
    void handleDisponibilidadException_RetornaConflict() {
        DisponibilidadException ex = new DisponibilidadException("Conflicto disponibilidad");

        ResponseEntity<Object> response =
                handler.handleDisponibilidadException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleGeneralException_RetornaInternalServerError() {
        Exception ex = new Exception("Error generico");

        ResponseEntity<Map<String, String>> response =
                handler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("Error interno"));
    }
}