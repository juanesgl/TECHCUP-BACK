package edu.dosw.proyect.exceptions;

import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.exceptions.DisponibilidadException;
import edu.dosw.proyect.core.exceptions.TournamentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

    @Test
    void businessRuleException_CreaCorrecto() {
        BusinessRuleException ex =
                new BusinessRuleException("Regla de negocio violada");

        assertEquals("Regla de negocio violada", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void resourceNotFoundException_CreaCorrecto() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Recurso no encontrado");

        assertEquals("Recurso no encontrado", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void disponibilidadException_CreaCorrecto() {
        DisponibilidadException ex =
                new DisponibilidadException("Error de disponibilidad");

        assertEquals("Error de disponibilidad", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void tournamentException_CreaCorrecto() {
        TournamentException ex =
                new TournamentException("Error de torneo");

        assertEquals("Error de torneo", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void businessRuleException_MensajeNulo_NoLanzaExcepcion() {
        assertDoesNotThrow(() -> new BusinessRuleException(null));
    }

    @Test
    void resourceNotFoundException_MensajeLargo_GuardaCompleto() {
        String mensaje = "Partido con ID 999 no encontrado en el sistema";
        ResourceNotFoundException ex = new ResourceNotFoundException(mensaje);

        assertEquals(mensaje, ex.getMessage());
    }
}