package edu.dosw.proyect.persistence.entitys;


import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticasTeamEntityTest {

    @Test
    void estadisticaEquipoEntity_Setters_FuncionanCorrectamente() {
        TeamStatisticsEntity e = new TeamStatisticsEntity();
        e.setPartidosJugados(5);
        e.setPartidosGanados(3);
        e.setPartidosEmpatados(1);
        e.setPartidosPerdidos(1);
        e.setGolesFavor(10);
        e.setGolesContra(4);
        e.setDiferenciaGol(6);
        e.setPuntos(10);

        assertEquals(5, e.getPartidosJugados());
        assertEquals(10, e.getPuntos());
        assertEquals(6, e.getDiferenciaGol());
    }
}
