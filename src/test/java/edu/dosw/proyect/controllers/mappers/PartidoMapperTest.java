package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartidoMapperTest {

    private final PartidoMapper mapper = new PartidoMapper();

    private Partido buildPartido() {
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        Equipo equipoLocal = new Equipo();
        equipoLocal.setNombre("Alpha");

        Equipo equipoVisitante = new Equipo();
        equipoVisitante.setNombre("Beta");

        Cancha cancha = new Cancha();
        cancha.setNombre("Cancha Principal");

        Student arbitro = new Student();
        arbitro.setName("Carlos Medina");

        Partido p = new Partido();
        p.setId(1L);
        p.setEquipoLocal(equipoLocal);
        p.setEquipoVisitante(equipoVisitante);
        p.setFechaHora(LocalDateTime.now());
        p.setCancha(cancha);
        p.setArbitro(arbitro);
        p.setEstado(MatchStatus.PROGRAMADO);
        p.setTorneo(torneo);
        return p;
    }

    @Test
    void toResponseDTO_HappyPath_Mapea_Correctamente() {
        Partido partido = buildPartido();

        PartidoResponseDTO dto = mapper.toResponseDTO(partido);

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

        PartidoResponseDTO dto = mapper.toResponseDTO(partido);

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

        List<PartidoResponseDTO> dtos = mapper.toResponseDTOList(partidos);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    @Test
    void toResponseDTOList_ListaVacia_RetornaListaVacia() {
        List<PartidoResponseDTO> dtos = mapper.toResponseDTOList(List.of());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}