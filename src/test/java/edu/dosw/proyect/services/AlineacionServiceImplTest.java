package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;
import edu.dosw.proyect.controllers.mappers.AlineacionMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import edu.dosw.proyect.persistence.repository.AlineacionRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import edu.dosw.proyect.core.services.impl.AlineacionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlineacionServiceImplTest {

    @Mock
    private PartidoRepository partidoRepository;

    @Mock
    private AlineacionRepository alineacionRepository;

    @Mock
    private AlineacionMapper alineacionMapper;

    @InjectMocks
    private AlineacionServiceImpl alineacionService;

    private Equipo buildEquipo(Long id, String nombre) {
        return Equipo.builder()
                .id(id)
                .nombre(nombre)
                .build();
    }

    private Partido buildPartido(Long id, Equipo local, Equipo visitante) {
        Partido p = new Partido();
        p.setId(id);
        p.setEquipoLocal(local);
        p.setEquipoVisitante(visitante);
        p.setNombreEquipoLocal(local.getNombre());
        p.setNombreEquipoVisitante(visitante.getNombre());
        p.setEstado(MatchStatus.PROGRAMADO);
        return p;
    }

    private Alineacion buildAlineacion(Long equipoId, String nombreEquipo) {
        Equipo equipo = buildEquipo(equipoId, nombreEquipo);

        Jugador jugador = new Jugador();
        jugador.setNombre("Jugador Test");

        AlineacionJugador titular = new AlineacionJugador();
        titular.setJugador(jugador);
        titular.setRol("TITULAR");

        AlineacionJugador reserva = new AlineacionJugador();
        reserva.setJugador(jugador);
        reserva.setRol("RESERVA");

        Alineacion a = new Alineacion();
        a.setId(1L);
        a.setEquipo(equipo);
        a.setFormacion(TacticalFormation.F_1_2_3_1);
        a.setJugadores(List.of(
                titular, titular, titular, titular,
                titular, titular, titular,
                reserva, reserva
        ));
        return a;
    }

    @Test
    void consultarAlineacionRival_HappyPath_SolicitanteEsLocal() {
        Equipo alpha = buildEquipo(1L, "Equipo Alpha");
        Equipo beta = buildEquipo(2L, "Equipo Beta");
        Partido partido = buildPartido(1L, alpha, beta);
        Alineacion alineacionBeta = buildAlineacion(2L, "Equipo Beta");

        AlineacionRivalResponseDTO expectedDTO = AlineacionRivalResponseDTO.builder()
                .partidoId(1L)
                .nombreEquipoRival("Equipo Beta")
                .formacion(TacticalFormation.F_1_2_3_1)
                .titulares(List.of("J1", "J2", "J3", "J4", "J5", "J6", "J7"))
                .reservas(List.of("J8", "J9"))
                .mensaje("Alineacion del equipo rival disponible")
                .build();

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(alineacionRepository.findByPartidoIdAndEquipoId(1L, 2L))
                .thenReturn(Optional.of(alineacionBeta));
        when(alineacionMapper.toRivalResponseDTO(alineacionBeta, 1L))
                .thenReturn(expectedDTO);

        AlineacionRivalResponseDTO resultado =
                alineacionService.consultarAlineacionRival(1L, 1L);

        assertNotNull(resultado);
        assertEquals("Equipo Beta", resultado.getNombreEquipoRival());
        assertEquals(7, resultado.getTitulares().size());
        verify(alineacionRepository, times(1))
                .findByPartidoIdAndEquipoId(1L, 2L);
    }

    @Test
    void consultarAlineacionRival_HappyPath_SolicitanteEsVisitante() {
        Equipo alpha = buildEquipo(1L, "Equipo Alpha");
        Equipo beta = buildEquipo(2L, "Equipo Beta");
        Partido partido = buildPartido(1L, alpha, beta);
        Alineacion alineacionAlpha = buildAlineacion(1L, "Equipo Alpha");

        AlineacionRivalResponseDTO expectedDTO = AlineacionRivalResponseDTO.builder()
                .partidoId(1L)
                .nombreEquipoRival("Equipo Alpha")
                .formacion(TacticalFormation.F_1_2_3_1)
                .titulares(List.of("J1", "J2", "J3", "J4", "J5", "J6", "J7"))
                .reservas(List.of("J8", "J9"))
                .mensaje("Alineacion del equipo rival disponible")
                .build();

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(alineacionRepository.findByPartidoIdAndEquipoId(1L, 1L))
                .thenReturn(Optional.of(alineacionAlpha));
        when(alineacionMapper.toRivalResponseDTO(alineacionAlpha, 1L))
                .thenReturn(expectedDTO);

        AlineacionRivalResponseDTO resultado =
                alineacionService.consultarAlineacionRival(1L, 2L);

        assertNotNull(resultado);
        assertEquals("Equipo Alpha", resultado.getNombreEquipoRival());
        verify(alineacionRepository, times(1))
                .findByPartidoIdAndEquipoId(1L, 1L);
    }


    @Test
    void consultarAlineacionRival_Error_PartidoNoEncontrado() {
        when(partidoRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> alineacionService.consultarAlineacionRival(999L, 1L)
        );

        assertTrue(ex.getMessage().contains("999"));
        verify(alineacionRepository, never())
                .findByPartidoIdAndEquipoId(any(), any());
    }

    @Test
    void consultarAlineacionRival_Error_EquipoNoParticipa() {
        Equipo alpha = buildEquipo(1L, "Equipo Alpha");
        Equipo beta = buildEquipo(2L, "Equipo Beta");
        Partido partido = buildPartido(1L, alpha, beta);

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));

        BusinessRuleException ex = assertThrows(
                BusinessRuleException.class,
                () -> alineacionService.consultarAlineacionRival(1L, 99L)
        );

        assertTrue(ex.getMessage().contains("no participa"));
        verify(alineacionRepository, never())
                .findByPartidoIdAndEquipoId(any(), any());
    }

    @Test
    void consultarAlineacionRival_Error_RivalSinAlineacion() {
        Equipo alpha = buildEquipo(1L, "Equipo Alpha");
        Equipo beta = buildEquipo(2L, "Equipo Beta");
        Partido partido = buildPartido(1L, alpha, beta);

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(alineacionRepository.findByPartidoIdAndEquipoId(1L, 2L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> alineacionService.consultarAlineacionRival(1L, 1L)
        );

        assertTrue(ex.getMessage().contains("todavía no ha registrado"));
        verify(alineacionRepository, times(1))
                .findByPartidoIdAndEquipoId(1L, 2L);
    }
}