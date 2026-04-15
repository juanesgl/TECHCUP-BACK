package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.AnswerInvitationRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitationResponseDTO;

public interface InvitationService {
    InvitationResponseDTO responderInvitacion(Long jugadorId, Long invitacionId, AnswerInvitationRequestDTO request);
}

