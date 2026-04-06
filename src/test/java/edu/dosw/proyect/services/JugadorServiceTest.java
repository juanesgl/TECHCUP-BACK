package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.DisponibilidadRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.DisponibilidadResponseDTO;
import edu.dosw.proyect.core.exceptions.DisponibilidadException;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JugadorServiceTest {

    @Mock
    private JugadorRepository jugadorRepository;

    @Mock
    private JugadorPersistenceMapper jugadorMapper;

    @InjectMocks
    private edu.dosw.proyect.core.services.JugadorService jugadorService;

    private JugadorEntity buildEntity(boolean perfilCompleto, boolean tieneEquipo,
                                      boolean disponible) {
        JugadorEntity e = new JugadorEntity();
        e.setId(1L);
        e.setPerfilCompleto(perfilCompleto);
        e.setTieneEquipo(tieneEquipo);
        e.setDisponible(disponible);
        return e;
    }

    private Jugador buildDomain(boolean perfilCompleto, boolean tieneEquipo) {
        return new Jugador(1L, "Juan", perfilCompleto, tieneEquipo, false);
    }

    @Test
    void actualizarDisponibilidad_Activar_HappyPath() {
        JugadorEntity entity = buildEntity(true, false, false);
        Jugador domain = buildDomain(true, false);
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jugadorMapper.toDomain(entity)).thenReturn(domain);
        when(jugadorRepository.save(any())).thenReturn(entity);

        DisponibilidadResponseDTO response =
                jugadorService.actualizarDisponibilidad(1L, request);

        assertNotNull(response);
        verify(jugadorRepository, times(1)).save(entity);
    }

    @Test
    void actualizarDisponibilidad_Desactivar_HappyPath() {
        JugadorEntity entity = buildEntity(true, false, true);
        Jugador domain = buildDomain(true, false);
        domain.setDisponible(true);
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(false);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jugadorMapper.toDomain(entity)).thenReturn(domain);
        when(jugadorRepository.save(any())).thenReturn(entity);

        DisponibilidadResponseDTO response =
                jugadorService.actualizarDisponibilidad(1L, request);

        assertNotNull(response);
    }

    @Test
    void actualizarDisponibilidad_JugadorNoExiste_LanzaException() {
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);
        assertThrows(DisponibilidadException.class,
                () -> jugadorService.actualizarDisponibilidad(99L, request));
    }

    @Test
    void actualizarDisponibilidad_SinPerfilCompleto_LanzaException() {
        JugadorEntity entity = buildEntity(false, false, false);
        Jugador domain = buildDomain(false, false);
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jugadorMapper.toDomain(entity)).thenReturn(domain);

        assertThrows(DisponibilidadException.class,
                () -> jugadorService.actualizarDisponibilidad(1L, request));
    }

    @Test
    void actualizarDisponibilidad_YaTieneEquipo_LanzaException() {
        JugadorEntity entity = buildEntity(true, true, false);
        Jugador domain = buildDomain(true, true);
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jugadorMapper.toDomain(entity)).thenReturn(domain);

        assertThrows(DisponibilidadException.class,
                () -> jugadorService.actualizarDisponibilidad(1L, request));
    }

    @Test
    void unirseAEquipo_HappyPath_ActualizaJugador() {
        JugadorEntity entity = buildEntity(true, false, true);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jugadorRepository.save(any())).thenReturn(entity);

        jugadorService.unirseAEquipo(1L, 2L);

        assertTrue(entity.isTieneEquipo());
        assertFalse(entity.isDisponible());
        verify(jugadorRepository, times(1)).save(entity);
    }

    @Test
    void unirseAEquipo_JugadorNoExiste_NoHaceNada() {
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        jugadorService.unirseAEquipo(99L, 2L);

        verify(jugadorRepository, never()).save(any());
    }
}