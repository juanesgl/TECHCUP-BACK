package edu.dosw.proyect.persistence.entitys;


import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.core.models.enums.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class MatchEntityTest {

    @Test
    void partidoEntity_Builder_ConstruyeCorrectamente() {
        MatchEntity p = MatchEntity.builder()
                .id(1L).golesLocal(2).golesVisitante(1)
                .estado(MatchStatus.FINALIZADO)
                .fase("Grupos")
                .fechaHora(LocalDateTime.now())
                .build();

        assertEquals(1L, p.getId());
        assertEquals(2, p.getGolesLocal());
        assertEquals(MatchStatus.FINALIZADO, p.getEstado());
    }

    @Test
    void partidoEntity_Setters_FuncionanCorrectamente() {
        MatchEntity p = new MatchEntity();
        p.setGolesLocal(3);
        p.setGolesVisitante(0);
        p.setEstado(MatchStatus.PROGRAMADO);

        assertEquals(3, p.getGolesLocal());
        assertEquals(MatchStatus.PROGRAMADO, p.getEstado());
    }
}
