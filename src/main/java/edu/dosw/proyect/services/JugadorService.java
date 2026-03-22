package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.DisponibilidadRequestDTO;
import edu.dosw.proyect.dtos.DisponibilidadResponseDTO;
import edu.dosw.proyect.exceptions.DisponibilidadException;
import edu.dosw.proyect.models.Jugador;
import edu.dosw.proyect.repositories.JugadorRepository;
import edu.dosw.proyect.utils.DisponibilidadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JugadorService {

    private final JugadorRepository jugadorRepository;

    public DisponibilidadResponseDTO actualizarDisponibilidad(Long jugadorId, DisponibilidadRequestDTO request) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new DisponibilidadException("El jugador especificado no existe"));

        boolean nuevoEstado = DisponibilidadMapper.mapRequestToStatus(request);

        if (nuevoEstado) {
            if (!jugador.isPerfilCompleto()) {
                throw new DisponibilidadException("Para marcarte como disponible, el perfil deportivo debe estar completo.");
            }

            if (jugador.isTieneEquipo()) {
                throw new DisponibilidadException("NO puedes marcarte como disponible porque ya perteneces a un equipo");
            }
        }

        jugador.setDisponible(nuevoEstado);
        jugadorRepository.save(jugador);
        
        String mensaje = nuevoEstado ? "Ahora estas visible para los capitanes." : "Ya no estas visible para los capitanes.";
        return DisponibilidadMapper.mapToResponse(jugador, mensaje);
    }

    public void unirseAEquipo(Long jugadorId, Long equipoId) {
        jugadorRepository.findById(jugadorId).ifPresent(jugador -> {
            jugador.setTieneEquipo(true);
            jugador.setDisponible(false);
            jugadorRepository.save(jugador);
        });
    }
}
