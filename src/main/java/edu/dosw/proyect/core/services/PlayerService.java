package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.PlayerResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private final JugadorRepository jugadorRepository;

    public List<PlayerResponse> filterPlayers(PlayerFilterRequest request) {

        logger.info("Iniciando busqueda de jugadores con filtros JPA: nombre={}, posicion={}, edad={}, semestre={}, disponible={}",
                request.getName(), request.getPosition(), request.getAge(), request.getSemester(), request.getAvailable());

        if (request.getName() == null && request.getPosition() == null && request.getAge() == null
                && request.getSemester() == null && request.getAvailable() == null) {
            logger.warn("Busqueda sin filtros - retornando todos los jugadores (no permitido por regla de negocio)");
            throw new BusinessException("Debe proporcionar al menos un filtro de busqueda");
        }

        List<Jugador> results = jugadorRepository.filterPlayers(
                request.getName(),
                request.getPosition(),
                request.getSemester(),
                request.getAvailable(),
                request.getAge());

        if (results.isEmpty()) {
            logger.warn("No se encontraron jugadores con los filtros aplicados");
            throw new BusinessException("No se encontraron jugadores con los filtros indicados");
        }

        logger.info("Busqueda JPA completada. Jugadores encontrados: {}", results.size());

        return results.stream()
                .map(j -> new PlayerResponse(
                        j.getId(),
                        j.getUsuario() != null ? j.getUsuario().getName() : "Sin nombre",
                        j.getPosiciones(),
                        j.getEdad()))
                .collect(Collectors.toList());
    }
}