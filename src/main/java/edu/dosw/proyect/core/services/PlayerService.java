package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.response.PlayerResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
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
        if (request.getName() == null && request.getPosition() == null
                && request.getAge() == null && request.getSemester() == null
                && request.getAvailable() == null) {
            throw new BusinessException("Debe proporcionar al menos un filtro de busqueda");
        }

        List<JugadorEntity> results = jugadorRepository.filterPlayers(
                request.getName(), request.getPosition(),
                request.getSemester(), request.getAvailable(), request.getAge());

        if (results.isEmpty()) {
            throw new BusinessException("No se encontrarón jugadores con los filtros indicados");
        }

        return results.stream()
                .map(j -> new PlayerResponse(
                        j.getId(),
                        j.getUsuario() != null ? j.getUsuario().getName() : "Sin nombre",
                        j.getPosiciones(),
                        j.getEdad()))
                .collect(Collectors.toList());
    }
}