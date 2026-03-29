package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;
import edu.dosw.proyect.controllers.mappers.AlineacionMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Alineacion;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.FormacionTecnica;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.repositories.AlineacionRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.services.impl.AlineacionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
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
        return Partido.builder()
                .id(id)
                .equipoLocal(local)
                .equipoVisitante(visitante)
                .nombreEquipoLocal(local.getNombre())
                .nombreEquipoVisitante(visitante.getNombre())
                .fecha(LocalDate.now().plusDays(3))
                .hora(LocalTime.of(10, 0))
                .cancha("Cancha Principal")
                .estado(MatchStatus.PROGRAMADO)
                .build();
    }

    private Alineacion buildAlineacion(Long partidoId, Long equipoId, String nombre) {
        return Alineacion.builder()
                .id(1L)
                .partidoId(partidoId)
                .equipoId(equipoId)
                .nombreEquipo(nombre)
                .formacion(FormacionTecnica.F_2_3_2)
                .titulares(List.of("J1", "J2", "J3", "J4", "J5", "J6", "J7"))
                .reservas(List.of("J8", "J9"))
                .build();
    }


    @Test
    void consultarAlineacionRival_HappyPath_SolicitanteEsLocal() {

        Equipo alpha = buildEquipo(1L, "Equipo Alpha");
        Equipo beta = buildEquipo(2L, "Equipo Beta");
        Partido partido = buildPartido(1L, alpha, beta);
        Alineacion alineacionBeta = buildAlineacion(1L, 2L, "Equipo Beta");

        AlineacionRivalResponseDTO expectedDTO = AlineacionRivalResponseDTO.builder()
                .partidoId(1L)
                .nombreEquipoRival("Equipo Beta")
                .formacion(FormacionTecnica.F_2_3_2)
                .titulares(List.of("J1", "J2", "J3", "J4", "J5", "J6", "J7"))
                .reservas(List.of("J8", "J9"))
                .mensaje("Alineación del equipo rival disponible")
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
        Alineacion alineacionAlpha = buildAlineacion(1L, 1L, "Equipo Alpha");

        AlineacionRivalResponseDTO expectedDTO = AlineacionRivalResponseDTO.builder()
                .partidoId(1L)
                .nombreEquipoRival("Equipo Alpha")
                .formacion(FormacionTecnica.F_2_3_2)
                .titulares(List.of("J1", "J2", "J3", "J4", "J5", "J6", "J7"))
                .reservas(List.of("J8", "J9"))
                .mensaje("Alineación del equipo rival disponible")
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