package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.AvailabilityRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.AvailabilityResponseDTO;
import edu.dosw.proyect.core.exceptions.DisponibilidadException;
import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.persistence.entity.PlayerEntity;
import edu.dosw.proyect.persistence.mapper.PlayerPersistenceMapper;
import edu.dosw.proyect.persistence.repository.PlayerRepository;
import edu.dosw.proyect.core.utils.AvailabilityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerAvailabilityService {

    private final PlayerRepository playerRepository;
    private final PlayerPersistenceMapper jugadorMapper;

    @Transactional
    public AvailabilityResponseDTO actualizarDisponibilidad(Long jugadorId,
                                                            AvailabilityRequestDTO request) {
        PlayerEntity entity = playerRepository.findById(jugadorId)
                .orElseThrow(() -> new DisponibilidadException("El jugador no existe"));

        Player jugador = jugadorMapper.toDomain(entity);
        boolean nuevoEstado = AvailabilityMapper.mapRequestToStatus(request);

        if (nuevoEstado) {
            if (!jugador.isPerfilCompleto()) {
                throw new DisponibilidadException(
                        "El perfil deportivo debe estar completo para marcarte disponible.");
            }
            if (jugador.isTieneEquipo()) {
                throw new DisponibilidadException(
                        "No puedes marcarte disponible porque ya perteneces a un equipo.");
            }
        }

        entity.setDisponible(nuevoEstado);
        playerRepository.save(entity);

        jugador.setDisponible(nuevoEstado);
        String mensaje = nuevoEstado ? "Ahora estas visible para los capitanes."
                : "Ya no estas visible para los capitanes.";
        return AvailabilityMapper.mapToResponse(jugador, mensaje);
    }

    @Transactional
    public void unirseAEquipo(Long jugadorId, Long equipoId) {
        playerRepository.findById(jugadorId).ifPresent(entity -> {
            entity.setTieneEquipo(true);
            entity.setDisponible(false);
            playerRepository.save(entity);
        });
    }
}