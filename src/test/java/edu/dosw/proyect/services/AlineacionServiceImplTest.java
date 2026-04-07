package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;
import edu.dosw.proyect.controllers.mappers.AlineacionMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Alineacion;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.services.impl.AlineacionServiceImpl;
import edu.dosw.proyect.persistence.entity.AlineacionEntity;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import edu.dosw.proyect.persistence.mapper.AlineacionPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.AlineacionRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlineacionServiceImplTest {

    @Mock private PartidoRepository partidoRepository;
    @Mock private AlineacionRepository alineacionRepository;
    @Mock private AlineacionMapper alineacionMapper;
    @Mock private PartidoPersistenceMapper partidoMapper;
    @Mock private AlineacionPersistenceMapper alineacionMapper2;

    @InjectMocks
    private AlineacionServiceImpl alineacionService;

    private Partido buildPartido(Long localId, Long visitanteId) {
        Equipo local = new Equipo();
        local.setId(localId);
        local.setNombre("Alpha");

        Equipo visitante = new Equipo();
        visitante.setId(visitanteId);
        visitante.setNombre("Beta");

        Partido p = new Partido();
        p.setId(1L);
        p.setEquipoLocal(local);
        p.setEquipoVisitante(visitante);
        return p;
    }

    @Test
    void consultarAlineacionRival_HappyPath_EquipoLocal() {
        PartidoEntity entity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);
        AlineacionEntity alineacionEntity = new AlineacionEntity();
        Alineacion alineacion = new Alineacion();
        AlineacionRivalResponseDTO dto = AlineacionRivalResponseDTO.builder()
                .partidoId(1L).nombreEquipoRival("Beta").build();

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(partido);
        when(alineacionRepository.findByPartidoIdAndEquipoId(1L, 2L))
                .thenReturn(Optional.of(alineacionEntity));
        when(alineacionMapper2.toDomain(alineacionEntity)).thenReturn(alineacion);
        when(alineacionMapper.toRivalResponseDTO(alineacion, 1L)).thenReturn(dto);

        AlineacionRivalResponseDTO result =
                alineacionService.consultarAlineacionRival(1L, 1L);

        assertNotNull(result);
        assertEquals("Beta", result.getNombreEquipoRival());
    }

    @Test
    void consultarAlineacionRival_HappyPath_EquipoVisitante() {
        PartidoEntity entity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);
        AlineacionEntity alineacionEntity = new AlineacionEntity();
        Alineacion alineacion = new Alineacion();
        AlineacionRivalResponseDTO dto = AlineacionRivalResponseDTO.builder()
                .partidoId(1L).nombreEquipoRival("Alpha").build();

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(partido);
        when(alineacionRepository.findByPartidoIdAndEquipoId(1L, 1L))
                .thenReturn(Optional.of(alineacionEntity));
        when(alineacionMapper2.toDomain(alineacionEntity)).thenReturn(alineacion);
        when(alineacionMapper.toRivalResponseDTO(alineacion, 1L)).thenReturn(dto);

        AlineacionRivalResponseDTO result =
                alineacionService.consultarAlineacionRival(1L, 2L);

        assertNotNull(result);
    }

    @Test
    void consultarAlineacionRival_PartidoNoEncontrado_LanzaException() {
        when(partidoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> alineacionService.consultarAlineacionRival(99L, 1L));
    }

    @Test
    void consultarAlineacionRival_EquipoNoParticipa_LanzaException() {
        PartidoEntity entity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(partido);

        assertThrows(BusinessRuleException.class,
                () -> alineacionService.consultarAlineacionRival(1L, 99L));
    }

    @Test
    void consultarAlineacionRival_AlineacionNoRegistrada_LanzaException() {
        PartidoEntity entity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(partido);
        when(alineacionRepository.findByPartidoIdAndEquipoId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> alineacionService.consultarAlineacionRival(1L, 1L));
    }
}