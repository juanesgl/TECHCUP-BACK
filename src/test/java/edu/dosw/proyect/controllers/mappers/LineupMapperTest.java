package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.OpponentLineupResponseDTO;
import edu.dosw.proyect.core.models.Lineup;
import edu.dosw.proyect.core.models.LineupPlayer;
import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LineupMapperTest {

    private final LineupMapper mapper = Mappers.getMapper(LineupMapper.class);

    private Player buildJugador(String nombre) {
        Player j = new Player();
        j.setNombre(nombre);
        return j;
    }

    private LineupPlayer buildAlineacionJugador(String nombre, String rol) {
        LineupPlayer aj = new LineupPlayer();
        aj.setJugador(buildJugador(nombre));
        aj.setRol(rol);
        return aj;
    }

    private Lineup buildAlineacion() {
        Team team = new Team();
        team.setNombre("Equipo Beta");

        Lineup a = new Lineup();
        a.setId(1L);
        a.setTeam(team);
        a.setFormacion(TacticalFormation.F_1_2_3_1);
        a.setJugadores(List.of(
                buildAlineacionJugador("J1", "TITULAR"),
                buildAlineacionJugador("J2", "TITULAR"),
                buildAlineacionJugador("J3", "TITULAR"),
                buildAlineacionJugador("J4", "TITULAR"),
                buildAlineacionJugador("J5", "TITULAR"),
                buildAlineacionJugador("J6", "TITULAR"),
                buildAlineacionJugador("J7", "TITULAR"),
                buildAlineacionJugador("J8", "RESERVA"),
                buildAlineacionJugador("J9", "RESERVA")));
        return a;
    }

    @Test
    void toRivalResponseDTO_HappyPath_MapeaCorrectamente() {
        Lineup lineup = buildAlineacion();

        OpponentLineupResponseDTO dto = mapper.toRivalResponseDTO(lineup, 1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getPartidoId());
        assertEquals("Equipo Beta", dto.getNombreEquipoRival());
        assertEquals(TacticalFormation.F_1_2_3_1, dto.getFormacion());
        assertEquals(7, dto.getTitulares().size());
        assertEquals(2, dto.getReservas().size());
        assertEquals("Alineación del equipo rival disponible", dto.getMensaje());
    }

    @Test
    void toRivalResponseDTO_SinJugadores_RetornaListasVacias() {
        Team team = new Team();
        team.setNombre("Equipo Vacio");

        Lineup lineup = new Lineup();
        lineup.setId(2L);
        lineup.setTeam(team);
        lineup.setFormacion(TacticalFormation.F_1_2_3_1);
        lineup.setJugadores(List.of());

        OpponentLineupResponseDTO dto = mapper.toRivalResponseDTO(lineup, 2L);

        assertNotNull(dto);
        assertTrue(dto.getTitulares().isEmpty());
        assertTrue(dto.getReservas().isEmpty());
    }

    @Test
    void toRivalResponseDTO_SoloTitulares_ReservasVacias() {
        Team team = new Team();
        team.setNombre("Equipo Solo Titulares");

        Lineup lineup = new Lineup();
        lineup.setId(3L);
        lineup.setTeam(team);
        lineup.setFormacion(TacticalFormation.F_1_2_3_1);
        lineup.setJugadores(List.of(
                buildAlineacionJugador("J1", "TITULAR"),
                buildAlineacionJugador("J2", "TITULAR"),
                buildAlineacionJugador("J3", "TITULAR"),
                buildAlineacionJugador("J4", "TITULAR"),
                buildAlineacionJugador("J5", "TITULAR"),
                buildAlineacionJugador("J6", "TITULAR"),
                buildAlineacionJugador("J7", "TITULAR")));

        OpponentLineupResponseDTO dto = mapper.toRivalResponseDTO(lineup, 3L);

        assertEquals(7, dto.getTitulares().size());
        assertTrue(dto.getReservas().isEmpty());
    }

    @Test
    void toRivalResponseDTO_SobrescribePartidoIdExplicito() {
        Lineup lineup = buildAlineacion();
        lineup.setId(999L);

        OpponentLineupResponseDTO dto = mapper.toRivalResponseDTO(lineup, 55L);

        assertEquals(55L, dto.getPartidoId());
    }
}