package edu.dosw.proyect.controllers.dtos.responses;
import edu.dosw.proyect.controllers.dtos.request.CanchaDTO;
import edu.dosw.proyect.controllers.dtos.response.*;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseDTOsTest {


    @Test
    void partidoResponseDTO_Builder_ConstruyeCorrectamente() {
        PartidoResponseDTO dto = PartidoResponseDTO.builder()
                .id(1L)
                .equipoLocal("Alpha")
                .equipoVisitante("Beta")
                .fecha(LocalDate.now())
                .hora(LocalTime.of(10, 0))
                .cancha("Cancha Principal")
                .arbitro("Carlos Medina")
                .estado("PROGRAMADO")
                .tournamentId("TOURN-1")
                .build();

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
    void partidoResponseDTO_NoArgsConstructor_CreaVacio() {
        PartidoResponseDTO dto = new PartidoResponseDTO();
        assertNull(dto.getId());
        assertNull(dto.getEquipoLocal());
        assertNull(dto.getEstado());
    }

    @Test
    void partidoResponseDTO_Setters_FuncionanCorrectamente() {
        PartidoResponseDTO dto = new PartidoResponseDTO();
        dto.setId(5L);
        dto.setEquipoLocal("Gamma");
        dto.setEstado("FINALIZADO");

        assertEquals(5L, dto.getId());
        assertEquals("Gamma", dto.getEquipoLocal());
        assertEquals("FINALIZADO", dto.getEstado());
    }


    @Test
    void alineacionRivalResponseDTO_Builder_ConstruyeCorrectamente() {
        AlineacionRivalResponseDTO dto = AlineacionRivalResponseDTO.builder()
                .partidoId(1L)
                .nombreEquipoRival("Equipo Beta")
                .formacion(TacticalFormation.F_1_2_3_1)
                .titulares(List.of("J1", "J2", "J3", "J4", "J5", "J6", "J7"))
                .reservas(List.of("J8", "J9"))
                .mensaje("Alineación del equipo rival disponible")
                .build();

        assertEquals(1L, dto.getPartidoId());
        assertEquals("Equipo Beta", dto.getNombreEquipoRival());
        assertEquals(TacticalFormation.F_1_2_3_1, dto.getFormacion());
        assertEquals(7, dto.getTitulares().size());
        assertEquals(2, dto.getReservas().size());
        assertNotNull(dto.getMensaje());
    }

    @Test
    void alineacionRivalResponseDTO_NoArgsConstructor_CreaVacio() {
        AlineacionRivalResponseDTO dto = new AlineacionRivalResponseDTO();
        assertNull(dto.getPartidoId());
        assertNull(dto.getNombreEquipoRival());
        assertNull(dto.getTitulares());
    }

    @Test
    void alineacionRivalResponseDTO_Setters_FuncionanCorrectamente() {
        AlineacionRivalResponseDTO dto = new AlineacionRivalResponseDTO();
        dto.setPartidoId(2L);
        dto.setNombreEquipoRival("Gamma");
        dto.setTitulares(List.of("J1", "J2"));

        assertEquals(2L, dto.getPartidoId());
        assertEquals("Gamma", dto.getNombreEquipoRival());
        assertEquals(2, dto.getTitulares().size());
    }


    @Test
    void configuracionTorneoResponseDTO_Builder_ConstruyeCorrectamente() {
        CanchaDTO cancha = new CanchaDTO();
        cancha.setNombre("Cancha Norte");

        ConfiguracionTorneoResponseDTO dto = ConfiguracionTorneoResponseDTO.builder()
                .message("Configuracion exitosa")
                .tournId("TOURN-1")
                .registrationCloseDate(LocalDate.now().plusMonths(1))
                .canchas(List.of(cancha))
                .build();

        assertEquals("Configuracion exitosa", dto.getMessage());
        assertEquals("TOURN-1", dto.getTournId());
        assertNotNull(dto.getRegistrationCloseDate());
        assertEquals(1, dto.getCanchas().size());
    }

    @Test
    void configuracionTorneoResponseDTO_NoArgsConstructor_CreaVacio() {
        ConfiguracionTorneoResponseDTO dto = new ConfiguracionTorneoResponseDTO();
        assertNull(dto.getMessage());
        assertNull(dto.getTournId());
        assertNull(dto.getCanchas());
    }


    @Test
    void estadisticasEquipoDTO_Builder_ConstruyeCorrectamente() {
        EstadisticasEquipoDTO dto = EstadisticasEquipoDTO.builder()
                .equipoId(1L)
                .nombreEquipo("Alpha FC")
                .partidosJugados(10)
                .victorias(6)
                .empates(2)
                .derrotas(2)
                .golesFavor(20)
                .golesContra(10)
                .diferenciaGol(10)
                .puntos(20)
                .build();

        assertEquals(1L, dto.getEquipoId());
        assertEquals("Alpha FC", dto.getNombreEquipo());
        assertEquals(10, dto.getPartidosJugados());
        assertEquals(6, dto.getVictorias());
        assertEquals(2, dto.getEmpates());
        assertEquals(2, dto.getDerrotas());
        assertEquals(20, dto.getGolesFavor());
        assertEquals(10, dto.getGolesContra());
        assertEquals(10, dto.getDiferenciaGol());
        assertEquals(20, dto.getPuntos());
    }

    @Test
    void estadisticasEquipoDTO_Setters_FuncionanCorrectamente() {
        EstadisticasEquipoDTO dto = new EstadisticasEquipoDTO();
        dto.setEquipoId(2L);
        dto.setNombreEquipo("Beta FC");
        dto.setPartidosJugados(5);
        dto.setVictorias(3);
        dto.setEmpates(1);
        dto.setDerrotas(1);
        dto.setGolesFavor(10);
        dto.setGolesContra(5);
        dto.setDiferenciaGol(5);
        dto.setPuntos(10);

        assertEquals(2L, dto.getEquipoId());
        assertEquals("Beta FC", dto.getNombreEquipo());
        assertEquals(5, dto.getPartidosJugados());
        assertEquals(3, dto.getVictorias());
        assertEquals(10, dto.getPuntos());
    }


    @Test
    void invitacionResponseDTO_Builder_ConstruyeCorrectamente() {
        InvitacionResponseDTO dto = InvitacionResponseDTO.builder()
                .invitacionId(1L)
                .mensajeCapitan("Bienvenido al equipo")
                .estadoActualizado("ACEPTADA")
                .build();

        assertEquals(1L, dto.getInvitacionId());
        assertEquals("Bienvenido al equipo", dto.getMensajeCapitan());
        assertEquals("ACEPTADA", dto.getEstadoActualizado());
    }

    @Test
    void invitacionResponseDTO_Setters_FuncionanCorrectamente() {
        InvitacionResponseDTO dto = InvitacionResponseDTO.builder()
                .invitacionId(1L)
                .mensajeCapitan("Test")
                .estadoActualizado("PENDIENTE")
                .build();

        dto.setEstadoActualizado("RECHAZADA");

        assertEquals("RECHAZADA", dto.getEstadoActualizado());
    }


    @Test
    void standingsTableResponseDTO_Builder_ConstruyeCorrectamente() {
        StandingsTableResponseDTO dto = StandingsTableResponseDTO.builder()
                .tournamentId("TOURN-1")
                .tournamentName("TechCup 2026")
                .totalTeams(8)
                .totalMatchesPlayed(12)
                .standings(List.of())
                .build();

        assertEquals("TOURN-1", dto.getTournamentId());
        assertEquals("TechCup 2026", dto.getTournamentName());
        assertEquals(8, dto.getTotalTeams());
        assertEquals(12, dto.getTotalMatchesPlayed());
        assertNotNull(dto.getStandings());
    }

    @Test
    void standingsTableResponseDTO_NoArgsConstructor_CreaVacio() {
        StandingsTableResponseDTO dto = new StandingsTableResponseDTO();
        assertNull(dto.getTournamentId());
        assertEquals(0, dto.getTotalTeams());
        assertNull(dto.getStandings());
    }
}