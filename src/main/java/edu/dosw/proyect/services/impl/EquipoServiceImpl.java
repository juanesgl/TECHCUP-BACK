package edu.dosw.proyect.services.impl;

import edu.dosw.proyect.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.exceptions.BusinessRuleException;
import edu.dosw.proyect.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.mappers.EquipoMapper;
import edu.dosw.proyect.models.Equipo;
import edu.dosw.proyect.models.User;
import edu.dosw.proyect.repositories.EquipoRepository;
import edu.dosw.proyect.repositories.InvitacionRepository;
import edu.dosw.proyect.repositories.UserRepository;
import edu.dosw.proyect.models.Invitacion;
import edu.dosw.proyect.models.enums.EstadoInvitacion;
import edu.dosw.proyect.services.EquipoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;
    private final UserRepository userRepository;
    private final InvitacionRepository invitacionRepository;
    private final EquipoMapper equipoMapper;

    @Override
    public CrearEquipoResponseDTO crearEquipo(Long capitanId, CrearEquipoRequestDTO request) {
        log.info("Iniciando creación de equipo, solicitada por el jugador ID: {}", capitanId);

        User capitan = userRepository.findById(capitanId)
                .orElseThrow(() -> new ResourceNotFoundException("Capitán no encontrado en el sistema"));

        if (capitan.getSportProfile() != null && capitan.getSportProfile().getEquipoActual() != null) {
            log.warn("El capitán con ID {} no puede liderar otro equipo", capitanId);
            throw new BusinessRuleException("El capitán ya pertenece a un equipo activo");
        }

        if (equipoRepository.existsByNombre(request.getNombreEquipo())) {
            log.warn("Violación TH-01: El nombre de equipo '{}' está registrado", request.getNombreEquipo());
            throw new BusinessRuleException("Ya existe un equipo con ese nombre en el torneo");
        }

        List<User> integracionFinal = new ArrayList<>();
        List<String> notificaciones = new ArrayList<>();

        for (Long invitadoId : request.getJugadoresInvitadosIds()) {
            User invitado = userRepository.findById(invitadoId).orElse(null);
            if (invitado != null) {
                if (invitado.getSportProfile() != null && invitado.getSportProfile().getEquipoActual() != null) {
                    log.warn("Violación TH-02 interceptada - el jugador {} (ID {}) está bloqueado", invitado.getName(),
                            
                            invitadoId);
                            
                    notificaciones.add("El jugador " + invitado.getName()
                            + " ya pertenece a otro equipo y NO recibirá la invitación.");
                } else {
                    integracionFinal.add(invitado);
                    notificaciones.add("Se enviará invitación correctamente al jugador " + invitado.getName());
                }
                        
            } else {
                notificaciones
                        .add("Advertencia: No se halló en base de datos al jugador con identificador " + invitadoId);
            }
        }

        integracionFinal.add(capitan);

        if (integracionFinal.size() < 7) {
            log.error("Violación TH-03 en validación de mínimo: total válidos es {}", integracionFinal.size());
            throw new BusinessRuleException("error de validación de composición del equipo");
        }

        long conteoCarrerasFoco = 0;
        List<String> carrerasAdmitidas = Arrays.asList("sistemas", "ia", "ciberseguridad", "estadistica");

        for (User integrante : integracionFinal) {
            if (integrante.getProgramaAcademico() != null) {
                if (carrerasAdmitidas.contains(integrante.getProgramaAcademico().toLowerCase())) {
                    conteoCarrerasFoco++;
                }
            }
        }

                    
        double indiceValido = (double) conteoCarrerasFoco / integracionFinal.size();
        if (indiceValido <= 0.5) {
            log.error("Violación TH-03 en composición de carreras: {} validos de {} integrantes necesarios",
                    conteoCarrerasFoco, integracionFinal.size());
            throw new BusinessRuleException("error de validación de composición del equipo");
        }

        Equipo equipoArmado = Equipo.builder()
                .nombre(request.getNombreEquipo())
                .escudo(request.getEscudo())
                .coloresUniforme(request.getColoresUniforme())
                .capitan(capitan)
                .build();

        equipoRepository.save(equipoArmado);

        for (User integrante : integracionFinal) {
            if (!integrante.getId().equals(capitan.getId())) {
                Invitacion inv = new Invitacion(null, integrante, equipoArmado, capitan, EstadoInvitacion.PENDIENTE);
                invitacionRepository.save(inv);
            }
        }

        if (capitan.getSportProfile() != null) {
            capitan.getSportProfile().setEquipoActual(equipoArmado);
            userRepository.save(capitan);
        }

        log.info("Creación existosa completada en el sistema para el equipo '{}'", equipoArmado.getNombre());
        return equipoMapper.toCrearEquipoResponseDTO(
                "El equipo ha sido registrado exitosamente tras superar las reglas del torneo", notificaciones);
    }
}
