package edu.dosw.proyect.exceptions;

import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBusinessException_RetornaBadRequest() {
        BusinessException ex = new BusinessException("Error de negocio");

        ResponseEntity<Map<String, Object>> response = handler.handleBusiness(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de negocio", response.getBody().get("error"));
        assertEquals("Error de negocio", response.getBody().get("message"));
    }

    @Test
    void handleGenericException_RetornaInternalServerError() {
        Exception ex = new Exception("Error inesperado");

        ResponseEntity<Map<String, Object>> response = handler.handleGeneral(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().get("error"));
    }
}