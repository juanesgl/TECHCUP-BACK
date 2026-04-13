package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.controllers.mappers.EquipoMapper;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import edu.dosw.proyect.persistence.entity.InvitacionEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EquipoRepository;
import edu.dosw.proyect.persistence.repository.InvitacionRepository;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
import edu.dosw.proyect.persistence.repository.UserRepository;
import edu.dosw.proyect.core.services.EquipoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;
    private final UserRepository userRepository;
    private final JugadorRepository jugadorRepository;
    private final InvitacionRepository invitacionRepository;
    private final EquipoMapper equipoMapper;
    private final EquipoPersistenceMapper equipoPersistenceMapper;

    @Override
    public CrearEquipoResponseDTO crearEquipo(Long capitanId, CrearEquipoRequestDTO request) {
        log.info("Iniciando creacion de equipo por capitan ID: {}", capitanId);

        UserEntity capitan = userRepository.findById(capitanId)
                .orElseThrow(() -> new ResourceNotFoundException("Capitan no encontrado"));

        if (equipoRepository.existsByNombre(request.getNombreEquipo())) {
            throw new BusinessRuleException("Ya existe un equipo con ese nombre en el torneo");
        }

        List<UserEntity> integracionFinal = new ArrayList<>();
        List<String> notificaciones = new ArrayList<>();

        for (Long invitadoId : request.getJugadoresInvitadosIds()) {
            UserEntity invitado = userRepository.findById(invitadoId).orElse(null);
            if (invitado != null) {
                integracionFinal.add(invitado);
                notificaciones.add("Invitacion enviada a " + invitado.getName());
            } else {
                notificaciones.add("Jugador con ID " + invitadoId + " no encontrado");
            }
        }

        integracionFinal.add(capitan);

        if (integracionFinal.size() < 7) {
            throw new BusinessRuleException("error de validacion de composicion del equipo");
        }

        List<String> carrerasAdmitidas = Arrays.asList("sistemas", "ia", "ciberseguridad", "estadistica");
        long conteoCarrerasFoco = integracionFinal.stream()
                .filter(u -> u.getAcademicProgram() != null &&
                        carrerasAdmitidas.contains(u.getAcademicProgram().toLowerCase()))
                .count();

        double indiceValido = (double) conteoCarrerasFoco / integracionFinal.size();
        if (indiceValido <= 0.5) {
            throw new BusinessRuleException("error de validacion de composicion del equipo");
        }

        EquipoEntity equipoEntity = EquipoEntity.builder()
                .nombre(request.getNombreEquipo())
                .escudoUrl(request.getEscudo())
                .colorUniformeLocal(request.getColoresUniforme())
                .capitan(jugadorRepository.findById(capitanId).orElse(null))
                .build();

        equipoRepository.save(equipoEntity);

        for (UserEntity integrante : integracionFinal) {
            if (!integrante.getId().equals(capitan.getId())) {
                InvitacionEntity inv = InvitacionEntity.builder()
                        .equipo(equipoEntity)
                        .jugador(jugadorRepository.findById(integrante.getId()).orElse(null))
                        .estado("PENDIENTE")
                        .fechaEnvio(LocalDateTime.now())
                        .build();
                invitacionRepository.save(inv);
            }
        }

        log.info("Equipo '{}' creado exitosamente", equipoEntity.getNombre());
        return equipoMapper.toCrearEquipoResponseDTO(
                "El equipo ha sido registrado exitosamente", notificaciones);
    }
}