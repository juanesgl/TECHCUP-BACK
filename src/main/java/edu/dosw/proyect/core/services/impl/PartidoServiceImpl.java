package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.PartidoFiltroRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.controllers.mappers.PartidoMapper;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import edu.dosw.proyect.core.services.PartidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartidoServiceImpl implements PartidoService {

    private final PartidoRepository partidoRepository;
    private final PartidoMapper partidoMapper;
    private final PartidoPersistenceMapper partidoPersistenceMapper;

    @Override
    public List<PartidoResponseDTO> consultarPartidos(PartidoFiltroRequestDTO filtro) {
        log.info("Consultando partidos con filtros: fecha={}, cancha={}, equipo={}, torneo={}",
                filtro.getFecha(), filtro.getCancha(),
                filtro.getNombreEquipo(), filtro.getTournamentId());

        List<PartidoEntity> resultado;

        if (filtro.getFecha() != null) {
            resultado = partidoRepository.findByFiltros(filtro.getFecha(), null);
        } else if (filtro.getNombreEquipo() != null && !filtro.getNombreEquipo().isBlank()) {
            resultado = partidoRepository.findByNombreEquipo(filtro.getNombreEquipo());
        } else if (filtro.getTournamentId() != null && !filtro.getTournamentId().isBlank()) {
            resultado = partidoRepository.findByTorneo_TournId(filtro.getTournamentId());
        } else {
            resultado = partidoRepository.findAll();
        }

        if (resultado.isEmpty()) {
            log.warn("No se encontraron partidos con los filtros aplicados");
            throw new ResourceNotFoundException(
                    "No hay partidos programados que coincidan con los filtros aplicados");
        }

        List<Partido> partidos = resultado.stream()
                .map(partidoPersistenceMapper::toDomain)
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
    public PartidoResponseDTO consultarPartidoPorId(Long partidoId) {
        log.info("Consultando detalle del partido ID: {}", partidoId);

        PartidoEntity entity = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partido con ID " + partidoId + " no encontrado en el sistema"));

        Partido partido = partidoPersistenceMapper.toDomain(entity);

        log.info("Partido encontrado: {} vs {}",
                partido.getEquipoLocal() != null ? partido.getEquipoLocal().getNombre() : "N/A",
                partido.getEquipoVisitante() != null ? partido.getEquipoVisitante().getNombre() : "N/A");

        return partidoMapper.toResponseDTO(partido);
    }
}