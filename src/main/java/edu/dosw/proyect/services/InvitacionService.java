package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.dtos.response.InvitacionResponseDTO;

public interface InvitacionService {
    InvitacionResponseDTO responderInvitacion(Long jugadorId, Long invitacionId, RespuestaInvitacionRequestDTO request);
}
