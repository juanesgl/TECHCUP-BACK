package edu.dosw.proyect.exceptions;

import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBusinessException_RetornaBadRequest() {
        BusinessException ex = new BusinessException("Error de negocio");

        ResponseEntity<PaymentResponse> response = handler.handleBusinessException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de negocio", response.getBody().getMessage());
        assertEquals("ERROR", response.getBody().getStatus());
    }

    @Test
    void handleGenericException_RetornaInternalServerError() {
        Exception ex = new Exception("Error inesperado");

        ResponseEntity<PaymentResponse> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertEquals("ERROR", response.getBody().getStatus());
    }
}