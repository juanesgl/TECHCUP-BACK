package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;
import edu.dosw.proyect.controllers.mappers.AlineacionMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Alineacion;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.persistence.entity.AlineacionEntity;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import edu.dosw.proyect.persistence.mapper.AlineacionPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.AlineacionRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
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
    private final PartidoPersistenceMapper partidoMapper;
    private final AlineacionPersistenceMapper alineacionMapper2;

    @Override
    public AlineacionRivalResponseDTO consultarAlineacionRival(
            Long partidoId, Long equipoSolicitanteId) {

        log.info("Consultando alineacion rival — Partido ID: {}, Equipo solicitante ID: {}",
                partidoId, equipoSolicitanteId);

        PartidoEntity partidoEntity = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partido con ID " + partidoId + " no encontrado"));

        Partido partido = partidoMapper.toDomain(partidoEntity);

        Long idEquipoLocal = partido.getEquipoLocal() != null
                ? partido.getEquipoLocal().getId() : null;
        Long idEquipoVisitante = partido.getEquipoVisitante() != null
                ? partido.getEquipoVisitante().getId() : null;

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

        AlineacionEntity alineacionEntity = alineacionRepository
                .findByPartidoIdAndEquipoId(partidoId, equipoRivalId)
                .orElseThrow(() -> {
                    log.warn("TH-01: El equipo rival '{}' no ha registrado su alineacion " +
                            "para el partido ID {}", nombreRival, partidoId);
                    return new ResourceNotFoundException(
                            "El equipo rival " + nombreRival +
                                    " todavia no ha registrado su alineacion para este partido");
                });

        Alineacion alineacion = alineacionMapper2.toDomain(alineacionEntity);

        log.info("Alineacion rival encontrada para el partido ID {}", partidoId);
        return alineacionMapper.toRivalResponseDTO(alineacion, partidoId);
    }
}