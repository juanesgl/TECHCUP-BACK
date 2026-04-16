package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.MatchFilterRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.MatchResponseDTO;
import edu.dosw.proyect.controllers.mappers.PartidoMapper;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.persistence.entity.MatchEntity;
import edu.dosw.proyect.persistence.mapper.MatchPersistenceMapper;
import edu.dosw.proyect.persistence.repository.MatchRepository;
import edu.dosw.proyect.core.services.PartidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements PartidoService {

    private final MatchRepository matchRepository;
    private final PartidoMapper partidoMapper;
    private final MatchPersistenceMapper matchPersistenceMapper;

    @Override
    public List<MatchResponseDTO> consultarPartidos(MatchFilterRequestDTO filtro) {
        log.info("Consultando partidos con filtros: fecha={}, cancha={}, equipo={}, torneo={}",
                filtro.getFecha(), filtro.getCancha(),
                filtro.getNombreEquipo(), filtro.getTournamentId());

        List<MatchEntity> resultado;

        if (filtro.getFecha() != null) {
            resultado = matchRepository.findByFiltros(filtro.getFecha(), null);
        } else if (filtro.getNombreEquipo() != null && !filtro.getNombreEquipo().isBlank()) {
            resultado = matchRepository.findByNombreEquipo(filtro.getNombreEquipo());
        } else if (filtro.getTournamentId() != null && !filtro.getTournamentId().isBlank()) {
            resultado = matchRepository.findByTorneo_TournId(filtro.getTournamentId());
        } else {
            resultado = matchRepository.findAll();
        }

        if (resultado.isEmpty()) {
            log.warn("No se encontraron partidos con los filtros aplicados");
            throw new ResourceNotFoundException(
                    "No hay partidos programados que coincidan con los filtros aplicados");
        }

        List<Partido> partidos = resultado.stream()
                .map(matchPersistenceMapper::toDomain)
                .collect(Collectors.toList());

        partidos.sort((a, b) -> {
            if (a.getFechaHora() == null) return 1;
            if (b.getFechaHora() == null) return -1;
            return a.getFechaHora().compareTo(b.getFechaHora());
        });

        log.info("Se encontraron {} partidos", partidos.size());
        return partidoMapper.toResponseDTOList(partidos);
    }

    @Override
    public MatchResponseDTO consultarPartidoPorId(Long partidoId) {
        log.info("Consultando detalle del partido ID: {}", partidoId);

        MatchEntity entity = matchRepository.findById(partidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partido con ID " + partidoId + " no encontrado en el sistema"));

        Partido partido = matchPersistenceMapper.toDomain(entity);

        log.info("Partido encontrado: {} vs {}",
                partido.getTeamLocal() != null ? partido.getTeamLocal().getNombre() : "N/A",
                partido.getTeamVisitante() != null ? partido.getTeamVisitante().getNombre() : "N/A");

        return partidoMapper.toResponseDTO(partido);
    }
}