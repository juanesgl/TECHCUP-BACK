package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.response.OpponentLineupResponseDTO;
import edu.dosw.proyect.controllers.mappers.LineupMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Lineup;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.persistence.entity.LineupEntity;
import edu.dosw.proyect.persistence.entity.MatchEntity;
import edu.dosw.proyect.persistence.mapper.LineupPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.MatchPersistenceMapper;
import edu.dosw.proyect.persistence.repository.LineupRepository;
import edu.dosw.proyect.persistence.repository.MatchRepository;
import edu.dosw.proyect.core.services.LineupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineupServiceImpl implements LineupService {

    private final MatchRepository matchRepository;
    private final LineupRepository lineupRepository;
    private final LineupMapper lineupMapper;
    private final MatchPersistenceMapper partidoMapper;
    private final LineupPersistenceMapper alineacionMapper2;

    @Override
    public OpponentLineupResponseDTO consultarAlineacionRival(
            Long partidoId, Long equipoSolicitanteId) {

        log.info("Consultando alineacion rival — Partido ID: {}, Equipo solicitante ID: {}",
                partidoId, equipoSolicitanteId);

        MatchEntity matchEntity = matchRepository.findById(partidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partido con ID " + partidoId + " no encontrado"));

        Partido partido = partidoMapper.toDomain(matchEntity);

        Long idEquipoLocal = partido.getTeamLocal() != null
                ? partido.getTeamLocal().getId() : null;
        Long idEquipoVisitante = partido.getTeamVisitante() != null
                ? partido.getTeamVisitante().getId() : null;

        boolean esLocal = equipoSolicitanteId.equals(idEquipoLocal);
        boolean esVisitante = equipoSolicitanteId.equals(idEquipoVisitante);

        if (!esLocal && !esVisitante) {
            log.warn("El equipo ID {} no participa en el partido ID {}",
                    equipoSolicitanteId, partidoId);
            throw new BusinessRuleException("Tu equipo no participa en este partido");
        }

        Long equipoRivalId = esLocal ? idEquipoVisitante : idEquipoLocal;
        String nombreRival = esLocal
                ? partido.getNombreEquipoVisitante()
                : partido.getNombreEquipoLocal();

        log.info("Equipo rival identificado: {} (ID: {})", nombreRival, equipoRivalId);

        LineupEntity lineupEntity = lineupRepository
                .findByPartidoIdAndEquipoId(partidoId, equipoRivalId)
                .orElseThrow(() -> {
                    log.warn("TH-01: El equipo rival '{}' no ha registrado su alineacion " +
                            "para el partido ID {}", nombreRival, partidoId);
                    return new ResourceNotFoundException(
                            "El equipo rival " + nombreRival +
                                    " todavia no ha registrado su alineacion para este partido");
                });

        Lineup lineup = alineacionMapper2.toDomain(lineupEntity);

        log.info("Alineacion rival encontrada para el partido ID {}", partidoId);
        return lineupMapper.toRivalResponseDTO(lineup, partidoId);
    }
}