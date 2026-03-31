package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.core.services.InvitacionService;
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
class InvitacionControllerTest {

    @Mock
    private InvitacionService invitacionService;

    @InjectMocks
    private InvitacionController invitacionController;

    @Test
    void responderInvitacion_HappyPath_RetornaOk() {
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);

        InvitacionResponseDTO dto = InvitacionResponseDTO.builder()
                .invitacionId(1L)
                .estadoActualizado("ACEPTADA")
                .mensajeCapitan("Juan acepto")
                .build();

        when(invitacionService.responderInvitacion(1L, 1L, request))
                .thenReturn(dto);

        ResponseEntity<InvitacionResponseDTO> result =
                invitacionController.responderInvitacion(1L, 1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("ACEPTADA", result.getBody().getEstadoActualizado());
        verify(invitacionService, times(1))
                .responderInvitacion(1L, 1L, request);
    }
}