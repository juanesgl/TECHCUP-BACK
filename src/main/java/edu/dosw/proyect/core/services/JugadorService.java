package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.DisponibilidadRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.DisponibilidadResponseDTO;
import edu.dosw.proyect.core.exceptions.DisponibilidadException;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
import edu.dosw.proyect.core.utils.DisponibilidadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final JugadorPersistenceMapper jugadorMapper;

    @Transactional
    public DisponibilidadResponseDTO actualizarDisponibilidad(Long jugadorId,
                                                              DisponibilidadRequestDTO request) {
        JugadorEntity entity = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new DisponibilidadException("El jugador no existe"));

        Jugador jugador = jugadorMapper.toDomain(entity);
        boolean nuevoEstado = DisponibilidadMapper.mapRequestToStatus(request);

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
        jugadorRepository.save(entity);

        jugador.setDisponible(nuevoEstado);
        String mensaje = nuevoEstado ? "Ahora estas visible para los capitanes."
                : "Ya no estas visible para los capitanes.";
        return DisponibilidadMapper.mapToResponse(jugador, mensaje);
    }

    @Transactional
    public void unirseAEquipo(Long jugadorId, Long equipoId) {
        jugadorRepository.findById(jugadorId).ifPresent(entity -> {
            entity.setTieneEquipo(true);
            entity.setDisponible(false);
            jugadorRepository.save(entity);
        });
    }
}