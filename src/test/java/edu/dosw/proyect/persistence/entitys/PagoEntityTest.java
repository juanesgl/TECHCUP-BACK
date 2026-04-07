package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PagoEntityTest {

    @Test
    void pagoEntity_NoArgsConstructor_CreaVacio() {
        PagoEntity pago = new PagoEntity();
        assertNull(pago.getId());
        assertNull(pago.getEstado());
    }

    @Test
    void pagoEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        EquipoEntity equipo = new EquipoEntity();
        equipo.setId(1L);
        TournamentEntity torneo = new TournamentEntity();
        torneo.setId(1L);
        UserEntity revisador = new UserEntity();
        revisador.setId(1L);
        LocalDateTime now = LocalDateTime.now();

        PagoEntity pago = new PagoEntity(1L, equipo, torneo,
                "http://comprobante.pdf", "PENDIENTE", now, now, revisador);

        assertEquals(1L, pago.getId());
        assertEquals("PENDIENTE", pago.getEstado());
        assertNotNull(pago.getFechaSubida());
    }

    @Test
    void pagoEntity_Setters_FuncionanCorrectamente() {
        PagoEntity pago = new PagoEntity();
        pago.setId(1L);
        pago.setComprobanteUrl("http://comprobante.pdf");
        pago.setEstado("APROBADO");
        pago.setFechaSubida(LocalDateTime.now());
        pago.setFechaRevision(LocalDateTime.now());

        assertEquals(1L, pago.getId());
        assertEquals("APROBADO", pago.getEstado());
        assertNotNull(pago.getFechaSubida());
        assertNotNull(pago.getFechaRevision());
    }
}
