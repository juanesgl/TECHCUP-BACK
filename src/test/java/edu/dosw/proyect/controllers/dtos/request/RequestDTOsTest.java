package edu.dosw.proyect.controllers.dtos.request;

import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.core.models.enums.FieldPosition;
import edu.dosw.proyect.core.models.enums.InvitationResponse;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestDTOsTest {

    @Test
    void canchaDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        SoccerFieldDTO dto = new SoccerFieldDTO("Cancha Principal", "Calle 100");

        assertEquals("Cancha Principal", dto.getNombre());
        assertEquals("Calle 100", dto.getUbicacion());
    }

    @Test
    void canchaDTO_NoArgsConstructor_CreaVacio() {
        SoccerFieldDTO dto = new SoccerFieldDTO();
        assertNull(dto.getNombre());
        assertNull(dto.getUbicacion());
    }

    @Test
    void canchaDTO_Setters_FuncionanCorrectamente() {
        SoccerFieldDTO dto = new SoccerFieldDTO();
        dto.setNombre("Cancha Norte");
        dto.setUbicacion("Calle 200");

        assertEquals("Cancha Norte", dto.getNombre());
        assertEquals("Calle 200", dto.getUbicacion());
    }

    @Test
    void partidoFiltroRequestDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        MatchFilterRequestDTO dto = new MatchFilterRequestDTO(
                LocalDate.now(), "Cancha Principal",
                "Alpha FC", "TOURN-1");

        assertNotNull(dto.getFecha());
        assertEquals("Cancha Principal", dto.getCancha());
        assertEquals("Alpha FC", dto.getNombreEquipo());
        assertEquals("TOURN-1", dto.getTournamentId());
    }

    @Test
    void partidoFiltroRequestDTO_NoArgsConstructor_CreaVacio() {
        MatchFilterRequestDTO dto = new MatchFilterRequestDTO();
        assertNull(dto.getFecha());
        assertNull(dto.getCancha());
        assertNull(dto.getNombreEquipo());
    }

    @Test
    void partidoFiltroRequestDTO_Setters_FuncionanCorrectamente() {
        MatchFilterRequestDTO dto = new MatchFilterRequestDTO();
        dto.setCancha("Cancha Sur");
        dto.setNombreEquipo("Beta FC");
        dto.setTournamentId("TOURN-2");
        dto.setFecha(LocalDate.now());

        assertEquals("Cancha Sur", dto.getCancha());
        assertEquals("Beta FC", dto.getNombreEquipo());
        assertEquals("TOURN-2", dto.getTournamentId());
        assertNotNull(dto.getFecha());
    }

    @Test
    void respuestaInvitacionRequestDTO_Setters_FuncionanCorrectamente() {
        AnswerInvitationRequestDTO dto = new AnswerInvitationRequestDTO();
        dto.setRespuesta(InvitationResponse.ACEPTAR);

        assertEquals(InvitationResponse.ACEPTAR, dto.getRespuesta());
    }

    @Test
    void respuestaInvitacionRequestDTO_Rechazar_FuncionaCorrectamente() {
        AnswerInvitationRequestDTO dto = new AnswerInvitationRequestDTO();
        dto.setRespuesta(InvitationResponse.RECHAZAR);

        assertEquals(InvitationResponse.RECHAZAR, dto.getRespuesta());
    }

    @Test
    void starterEntryRequestDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        StarterEntryRequestDTO dto = new StarterEntryRequestDTO(
                1L, FieldPosition.FORWARD);

        assertEquals(1L, dto.getPlayerId());
        assertEquals(FieldPosition.FORWARD, dto.getFieldPosition());
    }

    @Test
    void starterEntryRequestDTO_NoArgsConstructor_CreaVacio() {
        StarterEntryRequestDTO dto = new StarterEntryRequestDTO();
        assertNull(dto.getPlayerId());
        assertNull(dto.getFieldPosition());
    }

    @Test
    void starterEntryRequestDTO_Setters_FuncionanCorrectamente() {
        StarterEntryRequestDTO dto = new StarterEntryRequestDTO();
        dto.setPlayerId(2L);
        dto.setFieldPosition(FieldPosition.GOALKEEPER);

        assertEquals(2L, dto.getPlayerId());
        assertEquals(FieldPosition.GOALKEEPER, dto.getFieldPosition());
    }

    @Test
    void saveLineupRequestDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        StarterEntryRequestDTO starter = new StarterEntryRequestDTO(
                1L, FieldPosition.FORWARD);

        SaveLineupRequestDTO dto = new SaveLineupRequestDTO(
                10L, 5L,
                TacticalFormation.F_1_2_3_1,
                List.of(starter, starter, starter,
                        starter, starter, starter, starter),
                List.of(8L, 9L));

        assertEquals(10L, dto.getTeamId());
        assertEquals(5L, dto.getMatchId());
        assertEquals(TacticalFormation.F_1_2_3_1, dto.getFormation());
        assertEquals(7, dto.getStarters().size());
        assertEquals(2, dto.getReserveIds().size());
    }

    @Test
    void saveLineupRequestDTO_NoArgsConstructor_CreaVacio() {
        SaveLineupRequestDTO dto = new SaveLineupRequestDTO();
        assertNull(dto.getTeamId());
        assertNull(dto.getMatchId());
        assertNull(dto.getFormation());
    }

    @Test
    void crearEquipoRequestDTO_Setters_FuncionanCorrectamente() {
        CreateTeamRequestDTO dto = new CreateTeamRequestDTO();
        dto.setNombreEquipo("Alpha FC");
        dto.setEscudo("alpha.png");
        dto.setColoresUniforme("Rojo y Blanco");
        dto.setJugadoresInvitadosIds(List.of(1L, 2L, 3L, 4L, 5L, 6L));

        assertEquals("Alpha FC", dto.getNombreEquipo());
        assertEquals("alpha.png", dto.getEscudo());
        assertEquals("Rojo y Blanco", dto.getColoresUniforme());
        assertEquals(6, dto.getJugadoresInvitadosIds().size());
    }

    @Test
    void configuracionTorneoRequestDTO_Builder_ConstruyeCorrectamente() {
        SoccerFieldDTO cancha = new SoccerFieldDTO("Cancha Principal", "Calle 100");

        TournamentConfigurationRequestDTO dto = TournamentConfigurationRequestDTO.builder()
                .registrationCloseDate(LocalDate.now().plusMonths(1))
                .canchas(List.of(cancha))
                .importantDates("Fecha final: Dic 2026")
                .sanctions("Tarjeta roja acumulada")
                .regulation("Reglamento general")
                .organizerId(1L)
                .build();

        assertNotNull(dto.getRegistrationCloseDate());
        assertEquals(1, dto.getCanchas().size());
        assertEquals("Reglamento general", dto.getRegulation());
        assertEquals(1L, dto.getOrganizerId());
    }

    @Test
    void configuracionTorneoRequestDTO_NoArgsConstructor_CreaVacio() {
        TournamentConfigurationRequestDTO dto = new TournamentConfigurationRequestDTO();
        assertNull(dto.getRegistrationCloseDate());
        assertNull(dto.getCanchas());
        assertNull(dto.getOrganizerId());
    }

    @Test
    void tournamentRequest_Record_ConstruyeCorrectamente() {
        TournamentRequest request = new TournamentRequest(
                "TechCup 2026",
                LocalDate.now(),
                LocalDate.now().plusMonths(2),
                8,
                50000.0,
                "Reglamento general");

        assertEquals("TechCup 2026", request.name());
        assertNotNull(request.startDate());
        assertNotNull(request.endDate());
        assertEquals(8, request.maxTeams());
        assertEquals(50000, request.costPerTeam());
        assertEquals("Reglamento general", request.regulation());
    }

    @Test
    void disponibilidadRequestDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        AvailabilityRequestDTO dto = new AvailabilityRequestDTO(true);
        assertTrue(dto.getEstadoDisponibilidad());
    }

    @Test
    void disponibilidadRequestDTO_NoArgsConstructor_CreaVacio() {
        AvailabilityRequestDTO dto = new AvailabilityRequestDTO();
        assertNull(dto.getEstadoDisponibilidad());
    }

    @Test
    void disponibilidadRequestDTO_Setters_FuncionanCorrectamente() {
        AvailabilityRequestDTO dto = new AvailabilityRequestDTO();
        dto.setEstadoDisponibilidad(false);
        assertFalse(dto.getEstadoDisponibilidad());
    }

    @Test
    void loginRequestDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        LoginRequestDTO dto = new LoginRequestDTO(
                "juan@mail.com", "pass123");

        assertEquals("juan@mail.com", dto.getEmail());
        assertEquals("pass123", dto.getPassword());
    }

    @Test
    void loginRequestDTO_NoArgsConstructor_CreaVacio() {
        LoginRequestDTO dto = new LoginRequestDTO();
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
    }

    @Test
    void loginRequestDTO_Setters_FuncionanCorrectamente() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("test@mail.com");
        dto.setPassword("newpass");

        assertEquals("test@mail.com", dto.getEmail());
        assertEquals("newpass", dto.getPassword());
    }

    @Test
    void paymentStatusRequest_Setters_FuncionanCorrectamente() {
        PaymentStatusRequest dto = new PaymentStatusRequest();
        dto.setPaymentId(1L);
        dto.setStatus(PaymentStatus.PENDING);

        assertEquals(1L, dto.getPaymentId());
        assertEquals(PaymentStatus.PENDING, dto.getStatus());
    }

    @Test
    void paymentStatusRequest_StatusApproved_FuncionaCorrectamente() {
        PaymentStatusRequest dto = new PaymentStatusRequest();
        dto.setPaymentId(2L);
        dto.setStatus(PaymentStatus.APPROVED);

        assertEquals(PaymentStatus.APPROVED, dto.getStatus());
    }

    @Test
    void playerFilterRequest_Setters_FuncionanCorrectamente() {
        PlayerFilterRequest dto = new PlayerFilterRequest();
        dto.setName("Juan");
        dto.setPosition("Delantero");
        dto.setAge(22);
        dto.setSemester("5");
        dto.setAvailable(true);

        assertEquals("Juan", dto.getName());
        assertEquals("Delantero", dto.getPosition());
        assertEquals(22, dto.getAge());
        assertEquals("5", dto.getSemester());
        assertTrue(dto.getAvailable());
    }

    @Test
    void playerFilterRequest_NoArgsConstructor_CreaVacio() {
        PlayerFilterRequest dto = new PlayerFilterRequest();
        assertNull(dto.getName());
        assertNull(dto.getPosition());
        assertNull(dto.getAge());
        assertNull(dto.getAvailable());
    }

    @Test
    void registerMatchResultRequestDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        RegisterMatchResultRequestDTO dto = new RegisterMatchResultRequestDTO(3, 1);

        assertEquals(3, dto.getHomeGoals());
        assertEquals(1, dto.getAwayGoals());
    }

    @Test
    void registerMatchResultRequestDTO_NoArgsConstructor_CreaVacio() {
        RegisterMatchResultRequestDTO dto = new RegisterMatchResultRequestDTO();
        assertNull(dto.getHomeGoals());
        assertNull(dto.getAwayGoals());
    }

    @Test
    void registerMatchResultRequestDTO_Setters_FuncionanCorrectamente() {
        RegisterMatchResultRequestDTO dto = new RegisterMatchResultRequestDTO();
        dto.setHomeGoals(2);
        dto.setAwayGoals(0);

        assertEquals(2, dto.getHomeGoals());
        assertEquals(0, dto.getAwayGoals());
    }

    @Test
    void registerRequestDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Juan", "juan@mail.escuelaing.edu.co",
                "pass123", "STUDENT",
                "Delantero", 8);

        assertEquals("Juan", dto.getName());
        assertEquals("juan@mail.escuelaing.edu.co", dto.getEmail());
        assertEquals("pass123", dto.getPassword());
        assertEquals("STUDENT", dto.getRole());
        assertEquals("Delantero", dto.getPreferredPosition());
        assertEquals(8, dto.getSkillLevel());
    }

    @Test
    void registerRequestDTO_NoArgsConstructor_CreaVacio() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getRole());
        assertNull(dto.getSkillLevel());
    }

    @Test
    void registerRequestDTO_Setters_FuncionanCorrectamente() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setName("Maria");
        dto.setEmail("maria@mail.escuelaing.edu.co");
        dto.setRole("ADMIN");
        dto.setSkillLevel(9);

        assertEquals("Maria", dto.getName());
        assertEquals("ADMIN", dto.getRole());
        assertEquals(9, dto.getSkillLevel());
    }

    @Test
    void paymentUploadRequest_Setters_FuncionanCorrectamente() {
        PaymentUploadRequest dto = new PaymentUploadRequest();
        dto.setUserId(1);
        dto.setTournamentId(2);
        dto.setFileName("comprobante.pdf");
        dto.setFileUrl("http://url.com/comprobante.pdf");

        assertEquals(1, dto.getUserId());
        assertEquals(2, dto.getTournamentId());
        assertEquals("comprobante.pdf", dto.getFileName());
        assertEquals("http://url.com/comprobante.pdf", dto.getFileUrl());
    }

    @Test
    void paymentUploadRequest_NoArgsConstructor_CreaVacio() {
        PaymentUploadRequest dto = new PaymentUploadRequest();
        assertNull(dto.getUserId());
        assertNull(dto.getFileName());
    }

}
