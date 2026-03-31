package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PagoTest {

    @Test
    void pago_AllArgsConstructor_ConstruyeCorrectamente() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Alpha");

        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        User revisador = User.builder()
                .name("Admin")
                .email("admin@mail.com")
                .password("pass")
                .role("ADMIN")
                .build();

        LocalDateTime ahora = LocalDateTime.now();

        Pago pago = new Pago(
                1L, equipo, torneo,
                "comprobante.pdf", "PENDIENTE",
                ahora, ahora, revisador
        );

        assertEquals(1L, pago.getId());
        assertEquals("Alpha", pago.getEquipo().getNombre());
        assertEquals("TOURN-1", pago.getTorneo().getTournId());
        assertEquals("comprobante.pdf", pago.getComprobanteUrl());
        assertEquals("PENDIENTE", pago.getEstado());
        assertNotNull(pago.getFechaSubida());
        assertNotNull(pago.getFechaRevision());
        assertEquals("Admin", pago.getRevisadoPor().getName());
    }

    @Test
    void pago_NoArgsConstructor_CreaVacio() {
        Pago pago = new Pago();
        assertNull(pago.getId());
        assertNull(pago.getEstado());
        assertNull(pago.getComprobanteUrl());
    }

    @Test
    void pago_Setters_FuncionanCorrectamente() {
        Pago pago = new Pago();
        pago.setComprobanteUrl("nuevo.pdf");
        pago.setEstado("APROBADO");
        pago.setFechaSubida(LocalDateTime.now());
        pago.setFechaRevision(LocalDateTime.now());

        assertEquals("nuevo.pdf", pago.getComprobanteUrl());
        assertEquals("APROBADO", pago.getEstado());
        assertNotNull(pago.getFechaSubida());
        assertNotNull(pago.getFechaRevision());
    }
}