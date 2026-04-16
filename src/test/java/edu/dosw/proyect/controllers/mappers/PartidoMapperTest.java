package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.MatchResponseDTO;
import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartidoMapperTest {

    private final PartidoMapper mapper = Mappers.getMapper(PartidoMapper.class);

    private Partido buildPartido() {
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        Team teamLocal = new Team();
        teamLocal.setNombre("Alpha");

        Team teamVisitante = new Team();
        teamVisitante.setNombre("Beta");

        SoccerField soccerField = new SoccerField();
        soccerField.setNombre("Cancha Principal");

        Student arbitro = new Student();
        arbitro.setName("Carlos Medina");

        Partido p = new Partido();
        p.setId(1L);
        p.setTeamLocal(teamLocal);
        p.setTeamVisitante(teamVisitante);
        p.setFechaHora(LocalDateTime.now());
        p.setSoccerField(soccerField);
        p.setArbitro(arbitro);
        p.setEstado(MatchStatus.PROGRAMADO);
        p.setTorneo(torneo);
        return p;
    }

    @Test
    void toResponseDTO_HappyPath_Mapea_Correctamente() {
        Partido partido = buildPartido();

        MatchResponseDTO dto = mapper.toResponseDTO(partido);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Alpha", dto.getEquipoLocal());
        assertEquals("Beta", dto.getEquipoVisitante());
        assertEquals("Cancha Principal", dto.getCancha());
        assertEquals("Carlos Medina", dto.getArbitro());
        assertEquals("PROGRAMADO", dto.getEstado());
        assertEquals("TOURN-1", dto.getTournamentId());
        assertNotNull(dto.getFecha());
        assertNotNull(dto.getHora());
    }

    @Test
    void toResponseDTO_CamposNulos_RetornaTBD() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.setEstado(MatchStatus.PROGRAMADO);

        MatchResponseDTO dto = mapper.toResponseDTO(partido);

        assertNotNull(dto);
        assertEquals("TBD", dto.getEquipoLocal());
        assertEquals("TBD", dto.getEquipoVisitante());
        assertEquals("TBD", dto.getCancha());
        assertEquals("TBD", dto.getArbitro());
        assertNull(dto.getFecha());
        assertNull(dto.getHora());
        assertNull(dto.getTournamentId());
    }

    @Test
    void toResponseDTOList_HappyPath_MapeaLista() {
        List<Partido> partidos = List.of(buildPartido(), buildPartido());

        List<MatchResponseDTO> dtos = mapper.toResponseDTOList(partidos);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    @Test
    void toResponseDTOList_ListaVacia_RetornaListaVacia() {
        List<MatchResponseDTO> dtos = mapper.toResponseDTOList(List.of());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toLocalDate_AndToLocalTime_Null_RetornaNull() {
        assertNull(mapper.toLocalDate(null));
        assertNull(mapper.toLocalTime(null));
    }

    @Test
    void toLocalDate_AndToLocalTime_ConFechaHora_RetornaValores() {
        LocalDateTime fechaHora = LocalDateTime.of(2026, 4, 15, 21, 30, 0);

        assertEquals(2026, mapper.toLocalDate(fechaHora).getYear());
        assertEquals(LocalTime.of(21, 30), mapper.toLocalTime(fechaHora).withSecond(0));
    }
}