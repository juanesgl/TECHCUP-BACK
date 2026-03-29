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
        log.info("Iniciando creacion de equipo, solicitada por el jugador ID: {}", capitanId);

        User capitan = userRepository.findById(capitanId)
            .orElseThrow(() -> new ResourceNotFoundException("Capitan no encontrado en el sistema"));

        if (equipoRepository.existsByNombre(request.getNombreEquipo())) {
            log.warn("Violacion TH-01: El nombre de equipo '{}' esta registrado", request.getNombreEquipo());
            throw new BusinessRuleException("Ya existe un equipo con ese nombre en el torneo");
        }

        List<User> integracionFinal = new ArrayList<>();
        List<String> notificaciones = new ArrayList<>();

        for (Long invitadoId : request.getJugadoresInvitadosIds()) {
            User invitado = userRepository.findById(invitadoId).orElse(null);
            if (invitado != null) {
                integracionFinal.add(invitado);
                notificaciones.add("Se enviara invitacion correctamente al jugador " + invitado.getName());

            } else {
                notificaciones
                        .add("Advertencia: No se hallo en base de datos al jugador con identificador " + invitadoId);
            }
        }

        integracionFinal.add(capitan);

        if (integracionFinal.size() < 7) {
            log.error("Violacion TH-03 en validacion de minimo: total validos es {}", integracionFinal.size());
            throw new BusinessRuleException("error de validacion de composicion del equipo: se requieren al menos 7 integrantes validos");
        }

        long conteoCarrerasFoco = 0;
        long integrantesConPrograma = 0;
        List<String> carrerasAdmitidas = Arrays.asList("sistemas", "ia", "ciberseguridad", "estadistica");

        for (User integrante : integracionFinal) {
            if (integrante.getAcademicProgram() != null) {
                integrantesConPrograma++;
                if (carrerasAdmitidas.contains(integrante.getAcademicProgram().toLowerCase())) {
                    conteoCarrerasFoco++;
                }
            }
        }

        if (integrantesConPrograma > 0) {
            double indiceValido = (double) conteoCarrerasFoco / integrantesConPrograma;
            if (indiceValido <= 0.5) {
                log.error("Violacion TH-03 en composicion de carreras: {} validos de {} con programa academico",
                        conteoCarrerasFoco, integrantesConPrograma);
                throw new BusinessRuleException("error de validacion de composicion del equipo: menos del 50% cumple la composicion academica requerida");
            }
        } else {
            log.warn("Validacion academica omitida: no hay programa academico registrado en los integrantes del equipo");
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

        
        log.info("Creacion exitosa completada en el sistema para el equipo '{}'", equipoArmado.getNombre());
        return equipoMapper.toCrearEquipoResponseDTO(
            "El equipo ha sido registrado exitosamente tras superar las reglas del torneo", notificaciones);
    }
}

