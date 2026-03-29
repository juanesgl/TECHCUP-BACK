package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;
import edu.dosw.proyect.controllers.mappers.AlineacionMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Alineacion;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.repositories.AlineacionRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.services.AlineacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlineacionServiceImpl implements AlineacionService {

    private final PartidoRepository partidoRepository;
    private final AlineacionRepository alineacionRepository;
    private final AlineacionMapper alineacionMapper;

    @Override
    public AlineacionRivalResponseDTO consultarAlineacionRival(Long partidoId, Long equipoSolicitanteId) {
        log.info("Consultando alineación rival — Partido ID: {}, Equipo solicitante ID: {}",
                partidoId, equipoSolicitanteId);

        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partido con ID " + partidoId + " no encontrado"));

        Long idEquipoLocal = partido.getEquipoLocal() != null
                ? partido.getEquipoLocal().getId() : null;
        Long idEquipoVisitante = partido.getEquipoVisitante() != null
                ? partido.getEquipoVisitante().getId() : null;

        boolean esLocal = equipoSolicitanteId.equals(idEquipoLocal);
        boolean esVisitante = equipoSolicitanteId.equals(idEquipoVisitante);

        if (!esLocal && !esVisitante) {
            log.warn("El equipo ID {} no participa en el partido ID {}",
                    equipoSolicitanteId, partidoId);
            throw new BusinessRuleException(
                    "Tu equipo no participa en este partido");
        }

        Long equipoRivalId = esLocal ? idEquipoVisitante : idEquipoLocal;
        String nombreRival = esLocal
                ? partido.getNombreEquipoVisitante()
                : partido.getNombreEquipoLocal();

        log.info("Equipo rival identificado: {} (ID: {})", nombreRival, equipoRivalId);

        Alineacion alineacionRival = alineacionRepository
                .findByPartidoIdAndEquipoId(partidoId, equipoRivalId)
                .orElseThrow(() -> {
                    log.warn("TH-01: El equipo rival '{}' aún no ha registrado su alineación " +
                            "para el partido ID {}", nombreRival, partidoId);
                    return new ResourceNotFoundException(
                            "El equipo rival " + nombreRival +
                                    " todavía no ha registrado su alineación para este partido");
                });

        log.info("Alineación rival encontrada para el partido ID {}", partidoId);
        return alineacionMapper.toRivalResponseDTO(alineacionRival, partidoId);
    }
}