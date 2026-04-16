package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.CreateTeamResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamResponseDTO;
import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.Tournament;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamMapperTest {

    private final TeamMapper mapper = Mappers.getMapper(TeamMapper.class);

    @Test
    void toCrearEquipoResponseDTO_HappyPath_MapeaCorrectamente() {
        String mensaje = "Equipo creado exitosamente";
        List<String> notificaciones = List.of(
                "Invitacion enviada a Jugador 1",
                "Invitacion enviada a Jugador 2"
        );

        CreateTeamResponseDTO dto = mapper.toCrearEquipoResponseDTO(mensaje, notificaciones);

        assertNotNull(dto);
        assertEquals(mensaje, dto.getMensajeConfirmacion());
        assertEquals(2, dto.getNotificacionesEnviadas().size());
    }

    @Test
    void toCrearEquipoResponseDTO_ListaVacia_RetornaVacia() {
        CreateTeamResponseDTO dto = mapper.toCrearEquipoResponseDTO(
                "Equipo creado", List.of());

        assertNotNull(dto);
        assertTrue(dto.getNotificacionesEnviadas().isEmpty());
    }

    @Test
    void toResponseDTO_HappyPath_MapeaCampos() {
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");
        Player capitan = new Player();
        capitan.setId(9L);
        capitan.setNombre("Carlos");

        Team team = new Team();
        team.setId(1L);
        team.setNombre("Alpha FC");
        team.setEscudoUrl("escudo.png");
        team.setColorUniformeLocal("Rojo");
        team.setColorUniformeVisita("Blanco");
        team.setEstadoInscripcion("APROBADO");
        team.setTorneo(torneo);
        team.setCapitan(capitan);

        TeamResponseDTO dto = mapper.toResponseDTO(team);

        assertEquals(1L, dto.getEquipoId());
        assertEquals("Alpha FC", dto.getNombre());
        assertEquals("TOURN-1", dto.getTorneoId());
        assertEquals(9L, dto.getCapitanId());
        assertEquals("Carlos", dto.getCapitanNombre());
    }

    @Test
    void toResponseDTOList_HappyPath_RetornaLista() {
        Team team1 = new Team();
        team1.setId(1L);
        Team team2 = new Team();
        team2.setId(2L);

        List<TeamResponseDTO> dtos = mapper.toResponseDTOList(List.of(team1, team2));

        assertEquals(2, dtos.size());
    }
}