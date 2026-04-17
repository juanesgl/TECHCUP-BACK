package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.response.PlayerResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.persistence.entity.PlayerEntity;
import edu.dosw.proyect.persistence.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerFilterService {

    private final PlayerRepository playerRepository;

    public List<PlayerResponse> filterPlayers(PlayerFilterRequest request) {
        if (request.getName() == null && request.getPosition() == null
                && request.getAge() == null && request.getSemester() == null
                && request.getAvailable() == null) {
            throw new BusinessException("Debe proporcionar al menos un filtro de busqueda");
        }

        List<PlayerEntity> results = playerRepository.filterPlayers(
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