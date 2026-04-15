package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.AnswerInvitationRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitationResponseDTO;
import edu.dosw.proyect.core.models.enums.InvitationResponse;
import edu.dosw.proyect.core.services.InvitationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationControllerTest {

    @Mock
    private InvitationService invitationService;

    @InjectMocks
    private InvitationController invitationController;

    @Test
    void responderInvitacion_HappyPath_RetornaOk() {
        AnswerInvitationRequestDTO request = new AnswerInvitationRequestDTO();
        request.setRespuesta(InvitationResponse.ACEPTAR);

        InvitationResponseDTO dto = InvitationResponseDTO.builder()
                .invitacionId(1L)
                .estadoActualizado("ACEPTADA")
                .mensajeCapitan("Juan acepto")
                .build();

        when(invitationService.responderInvitacion(1L, 1L, request))
                .thenReturn(dto);

        ResponseEntity<InvitationResponseDTO> result =
                invitationController.responderInvitacion(1L, 1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("ACEPTADA", result.getBody().getEstadoActualizado());
        verify(invitationService, times(1))
                .responderInvitacion(1L, 1L, request);
    }
}