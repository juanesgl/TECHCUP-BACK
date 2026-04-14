package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.ActualizarEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.EquipoResponseDTO;
import edu.dosw.proyect.controllers.mappers.EquipoMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;

import edu.dosw.proyect.core.services.EquipoService;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import edu.dosw.proyect.persistence.entity.EquipoJugadorEntity;
import edu.dosw.proyect.persistence.entity.InvitacionEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipoServiceImpl implements EquipoService {

    private static final int MIN_JUGADORES = 8;
    private static final int MAX_JUGADORES = 12;
    private static final List<String> CARRERAS_ADMITIDAS =
            Arrays.asList("sistemas", "ia", "ciberseguridad", "estadistica");

    private final EquipoRepository        equipoRepository;
    private final UserRepository          userRepository;
    private final JugadorRepository       jugadorRepository;
    private final InvitacionRepository    invitacionRepository;
    private final EquipoJugadorRepository equipoJugadorRepository;
    private final TournamentRepository    torneoRepository;
    private final EquipoMapper            equipoMapper;
    private final EquipoPersistenceMapper equipoPersistenceMapper;

    @Override
    @Transactional
    public CrearEquipoResponseDTO crearEquipo(Long capitanId, CrearEquipoRequestDTO request) {
        log.info("Iniciando creación de equipo por capitán ID: {}", capitanId);

        UserEntity capitan = userRepository.findById(capitanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Capitán no encontrado con ID: " + capitanId));

        if (equipoRepository.existsByNombre(request.getNombreEquipo())) {
            throw new BusinessRuleException(
                    "Ya existe un equipo con el nombre: " + request.getNombreEquipo());
        }

        List<UserEntity> integrantes = new ArrayList<>();
        List<String> notificaciones = new ArrayList<>();

        for (Long invitadoId : request.getJugadoresInvitadosIds()) {
            UserEntity invitado = userRepository.findById(invitadoId).orElse(null);
            if (invitado != null) {
                integrantes.add(invitado);
                notificaciones.add("Invitación enviada a " + invitado.getName());
            } else {
                notificaciones.add("Jugador con ID " + invitadoId + " no encontrado");
            }
        }

        integrantes.add(capitan);

        if (integrantes.size() < MIN_JUGADORES) {
            throw new BusinessRuleException(
                    "El equipo debe tener mínimo " + MIN_JUGADORES +
                            " integrantes incluyendo el capitán. Actualmente: " +
                            integrantes.size());
        }

        if (integrantes.size() > MAX_JUGADORES) {
            throw new BusinessRuleException(
                    "El equipo no puede tener más de " + MAX_JUGADORES +
                            " integrantes. Actualmente: " + integrantes.size());
        }

        long conteoCarreras = integrantes.stream()
                .filter(u -> u.getAcademicProgram() != null &&
                        CARRERAS_ADMITIDAS.contains(
                                u.getAcademicProgram().toLowerCase()))
                .count();

        if ((double) conteoCarreras / integrantes.size() <= 0.5) {
            throw new BusinessRuleException(
                    "Más de la mitad del equipo debe ser del programa de " +
                            "Ingeniería de Sistemas, IA, Ciberseguridad o Estadística.");
        }

        for (UserEntity integrante : integrantes) {
            if (!integrante.getId().equals(capitanId)) {
                boolean tieneEquipo = jugadorRepository.findById(integrante.getId())
                        .map(j -> j.isTieneEquipo()).orElse(false);
                if (tieneEquipo) {
                    throw new BusinessRuleException(
                            "El jugador " + integrante.getName() +
                                    " ya pertenece a un equipo.");
                }
            }
        }

        EquipoEntity equipoEntity = EquipoEntity.builder()
                .nombre(request.getNombreEquipo())
                .escudoUrl(request.getEscudo())
                .colorUniformeLocal(request.getColoresUniforme())
                .capitan(jugadorRepository.findById(capitanId).orElse(null))
                .estadoInscripcion("PENDIENTE")
                .build();

        equipoRepository.save(equipoEntity);

        for (UserEntity integrante : integrantes) {
            if (!integrante.getId().equals(capitanId)) {
                InvitacionEntity inv = InvitacionEntity.builder()
                        .equipo(equipoEntity)
                        .jugador(jugadorRepository.findById(integrante.getId()).orElse(null))
                        .estado("PENDIENTE")
                        .fechaEnvio(LocalDateTime.now())
                        .build();
                invitacionRepository.save(inv);
            }
        }

        log.info("Equipo '{}' creado con ID: {}",
                equipoEntity.getNombre(), equipoEntity.getId());
        return equipoMapper.toCrearEquipoResponseDTO(
                "El equipo '" + request.getNombreEquipo() +
                        "' fue creado exitosamente. Se enviaron " +
                        (integrantes.size() - 1) + " invitaciones.",
                notificaciones);
    }

    @Override
    public EquipoResponseDTO consultarEquipo(Long equipoId) {
        log.info("Consultando equipo ID: {}", equipoId);
        EquipoEntity entity = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipo no encontrado con ID: " + equipoId));
        return equipoMapper.toResponseDTO(equipoPersistenceMapper.toDomain(entity));
    }

    @Override
    public List<EquipoResponseDTO> consultarEquiposPorTorneo(String tournamentId) {
        log.info("Consultando equipos del torneo: {}", tournamentId);
        var torneo = torneoRepository.findByTournId(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Torneo no encontrado: " + tournamentId));
        return equipoRepository.findByTorneoId(torneo.getId()).stream()
                .map(e -> equipoMapper.toResponseDTO(equipoPersistenceMapper.toDomain(e)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EquipoResponseDTO actualizarEquipo(Long equipoId, Long capitanId,
                                              ActualizarEquipoRequestDTO request) {
        log.info("Actualizando equipo ID: {} por capitán ID: {}", equipoId, capitanId);

        EquipoEntity equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipo no encontrado con ID: " + equipoId));

        validarEsCapitan(equipo, capitanId);

        if (request.getNombreEquipo() != null && !request.getNombreEquipo().isBlank()) {
            if (!request.getNombreEquipo().equals(equipo.getNombre()) &&
                    equipoRepository.existsByNombre(request.getNombreEquipo())) {
                throw new BusinessRuleException(
                        "Ya existe un equipo con el nombre: " + request.getNombreEquipo());
            }
            equipo.setNombre(request.getNombreEquipo());
        }
        if (request.getEscudo() != null && !request.getEscudo().isBlank()) {
            equipo.setEscudoUrl(request.getEscudo());
        }
        if (request.getColoresUniforme() != null && !request.getColoresUniforme().isBlank()) {
            equipo.setColorUniformeLocal(request.getColoresUniforme());
        }

        EquipoEntity updated = equipoRepository.save(equipo);
        log.info("Equipo ID: {} actualizado", equipoId);
        return equipoMapper.toResponseDTO(equipoPersistenceMapper.toDomain(updated));
    }

    @Override
    @Transactional
    public void eliminarEquipo(Long equipoId, Long capitanId) {
        log.info("Eliminando equipo ID: {} por capitán ID: {}", equipoId, capitanId);

        EquipoEntity equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipo no encontrado con ID: " + equipoId));

        validarEsCapitan(equipo, capitanId);

        equipoJugadorRepository.findByEquipoId(equipoId).forEach(ej -> {
            ej.getJugador().setTieneEquipo(false);
            ej.getJugador().setDisponible(true);
            jugadorRepository.save(ej.getJugador());
        });

        equipoRepository.delete(equipo);
        log.info("Equipo ID: {} eliminado", equipoId);
    }

    @Override
    public List<String> consultarJugadoresEquipo(Long equipoId) {
        log.info("Consultando jugadores del equipo ID: {}", equipoId);

        equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipo no encontrado con ID: " + equipoId));

        return equipoJugadorRepository.findByEquipoId(equipoId).stream()
                .filter(EquipoJugadorEntity::isActivo)
                .map(ej -> {
                    String nombre = ej.getJugador().getUsuario() != null
                            ? ej.getJugador().getUsuario().getName()
                            : "Jugador #" + ej.getJugador().getId();
                    return nombre + " (ID: " + ej.getJugador().getId() + ")";
                })
                .collect(Collectors.toList());
    }

    private void validarEsCapitan(EquipoEntity equipo, Long capitanId) {
        if (equipo.getCapitan() == null ||
                !equipo.getCapitan().getId().equals(capitanId)) {
            throw new BusinessRuleException(
                    "Solo el capitán del equipo puede realizar esta acción.");
        }
    }
}