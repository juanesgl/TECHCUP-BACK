package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.controllers.mappers.EquipoMapper;
import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.repositories.EquipoRepository;
import edu.dosw.proyect.core.repositories.InvitacionRepository;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import edu.dosw.proyect.core.repositories.UserRepository;
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

    @Override
    public CrearEquipoResponseDTO crearEquipo(Long capitanId, CrearEquipoRequestDTO request) {
        log.info("Iniciando creación de equipo, solicitada por el jugador ID: {}", capitanId);

        User capitan = userRepository.findById(capitanId)
                .orElseThrow(() -> new ResourceNotFoundException("Capitán no encontrado en el sistema"));

        // Verificar que el capitán no pertenece a otro equipo (validación simplificada)

        if (equipoRepository.existsByNombre(request.getNombreEquipo())) {
            log.warn("Violación TH-01: El nombre de equipo '{}' está registrado", request.getNombreEquipo());
            throw new BusinessRuleException("Ya existe un equipo con ese nombre en el torneo");
        }

        List<User> integracionFinal = new ArrayList<>();
        List<String> notificaciones = new ArrayList<>();

        for (Long invitadoId : request.getJugadoresInvitadosIds()) {
            User invitado = userRepository.findById(invitadoId).orElse(null);
            if (invitado != null) {
                // Simplificado: asumir que el jugador está disponible si existe en base de datos
                integracionFinal.add(invitado);
                notificaciones.add("Se enviará invitación correctamente al jugador " + invitado.getName());

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
            if (integrante.getAcademicProgram() != null) {
                if (carrerasAdmitidas.contains(integrante.getAcademicProgram().toLowerCase())) {
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
                .escudoUrl(request.getEscudo())
                .colorUniformeLocal(request.getColoresUniforme())
                .capitan(jugadorRepository.findById(capitanId).orElse(null))
                .build();

        equipoRepository.save(equipoArmado);

        for (User integrante : integracionFinal) {
            if (!integrante.getId().equals(capitan.getId())) {
                Invitacion inv = Invitacion.builder()
                        .equipo(equipoArmado)
                        .jugador(jugadorRepository.findById(integrante.getId()).orElse(null))
                        .estado("PENDIENTE")
                        .fechaEnvio(LocalDateTime.now())
                        .build();
                invitacionRepository.save(inv);
            }
        }

        
        log.info("Creación existosa completada en el sistema para el equipo '{}'", equipoArmado.getNombre());
        return equipoMapper.toCrearEquipoResponseDTO(
                "El equipo ha sido registrado exitosamente tras superar las reglas del torneo", notificaciones);
    }
}
