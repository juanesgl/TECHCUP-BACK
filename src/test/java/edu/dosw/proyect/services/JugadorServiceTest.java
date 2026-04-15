package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.AvailabilityRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.AvailabilityResponseDTO;
import edu.dosw.proyect.core.exceptions.DisponibilidadException;
import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.core.services.PlayerAvailabilityService;
import edu.dosw.proyect.persistence.entity.PlayerEntity;
import edu.dosw.proyect.persistence.mapper.PlayerPersistenceMapper;
import edu.dosw.proyect.persistence.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerFilterServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerPersistenceMapper jugadorMapper;

    @InjectMocks
    private PlayerAvailabilityService playerAvailabilityService;

    private PlayerEntity buildEntity(boolean perfilCompleto, boolean tieneEquipo,
                                     boolean disponible) {
        PlayerEntity e = new PlayerEntity();
        e.setId(1L);
        e.setPerfilCompleto(perfilCompleto);
        e.setTieneEquipo(tieneEquipo);
        e.setDisponible(disponible);
        return e;
    }

    private Player buildDomain(boolean perfilCompleto, boolean tieneEquipo) {
        return new Player(1L, "Juan", perfilCompleto, tieneEquipo, false);
    }

    @Test
    void actualizarDisponibilidad_Activar_HappyPath() {
        PlayerEntity entity = buildEntity(true, false, false);
        Player domain = buildDomain(true, false);
        AvailabilityRequestDTO request = new AvailabilityRequestDTO(true);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jugadorMapper.toDomain(entity)).thenReturn(domain);
        when(playerRepository.save(any())).thenReturn(entity);

        AvailabilityResponseDTO response =
                playerAvailabilityService.actualizarDisponibilidad(1L, request);

        assertNotNull(response);
        verify(playerRepository, times(1)).save(entity);
    }

    @Test
    void actualizarDisponibilidad_Desactivar_HappyPath() {
        PlayerEntity entity = buildEntity(true, false, true);
        Player domain = buildDomain(true, false);
        domain.setDisponible(true);
        AvailabilityRequestDTO request = new AvailabilityRequestDTO(false);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jugadorMapper.toDomain(entity)).thenReturn(domain);
        when(playerRepository.save(any())).thenReturn(entity);

        AvailabilityResponseDTO response =
                playerAvailabilityService.actualizarDisponibilidad(1L, request);

        assertNotNull(response);
    }

    @Test
    void actualizarDisponibilidad_JugadorNoExiste_LanzaException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());
        AvailabilityRequestDTO request = new AvailabilityRequestDTO(true);
        assertThrows(DisponibilidadException.class,
                () -> playerAvailabilityService.actualizarDisponibilidad(99L, request));
    }

    @Test
    void actualizarDisponibilidad_SinPerfilCompleto_LanzaException() {
        PlayerEntity entity = buildEntity(false, false, false);
        Player domain = buildDomain(false, false);
        AvailabilityRequestDTO request = new AvailabilityRequestDTO(true);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jugadorMapper.toDomain(entity)).thenReturn(domain);

        assertThrows(DisponibilidadException.class,
                () -> playerAvailabilityService.actualizarDisponibilidad(1L, request));
    }

    @Test
    void actualizarDisponibilidad_YaTieneEquipo_LanzaException() {
        PlayerEntity entity = buildEntity(true, true, false);
        Player domain = buildDomain(true, true);
        AvailabilityRequestDTO request = new AvailabilityRequestDTO(true);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jugadorMapper.toDomain(entity)).thenReturn(domain);

        assertThrows(DisponibilidadException.class,
                () -> playerAvailabilityService.actualizarDisponibilidad(1L, request));
    }

    @Test
    void unirseAEquipo_HappyPath_ActualizaJugador() {
        PlayerEntity entity = buildEntity(true, false, true);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(playerRepository.save(any())).thenReturn(entity);

        playerAvailabilityService.unirseAEquipo(1L, 2L);

        assertTrue(entity.isTieneEquipo());
        assertFalse(entity.isDisponible());
        verify(playerRepository, times(1)).save(entity);
    }

    @Test
    void unirseAEquipo_JugadorNoExiste_NoHaceNada() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        playerAvailabilityService.unirseAEquipo(99L, 2L);

        verify(playerRepository, never()).save(any());
    }
}