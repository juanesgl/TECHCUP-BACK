package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;

public interface InvitacionService {
    InvitacionResponseDTO responderInvitacion(Long jugadorId, Long invitacionId, RespuestaInvitacionRequestDTO request);
}

