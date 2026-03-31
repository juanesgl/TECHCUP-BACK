package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import edu.dosw.proyect.core.services.AlineacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlineacionControllerTest {

    @Mock
    private AlineacionService alineacionService;

    @InjectMocks
    private AlineacionController alineacionController;

    private AlineacionRivalResponseDTO buildDTO(String nombreRival) {
        return AlineacionRivalResponseDTO.builder()
                .partidoId(1L)
                .nombreEquipoRival(nombreRival)
                .formacion(TacticalFormation.F_1_2_3_1)
                .titulares(List.of("J1", "J2", "J3", "J4", "J5", "J6", "J7"))
                .reservas(List.of("J8", "J9"))
                .mensaje("Alineacion del equipo rival disponible")
                .build();
    }

    @Test
    void consultarAlineacionRival_HappyPath_LocalConsultaVisitante() {
        AlineacionRivalResponseDTO dto = buildDTO("Equipo Beta");
        when(alineacionService.consultarAlineacionRival(1L, 1L)).thenReturn(dto);

        ResponseEntity<AlineacionRivalResponseDTO> response =
                alineacionController.consultarAlineacionRival(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Equipo Beta", response.getBody().getNombreEquipoRival());
        assertEquals(7, response.getBody().getTitulares().size());
        verify(alineacionService, times(1))
                .consultarAlineacionRival(1L, 1L);
    }

    @Test
    void consultarAlineacionRival_HappyPath_VisitanteConsultaLocal() {
        AlineacionRivalResponseDTO dto = buildDTO("Equipo Alpha");
        when(alineacionService.consultarAlineacionRival(1L, 2L)).thenReturn(dto);

        ResponseEntity<AlineacionRivalResponseDTO> response =
                alineacionController.consultarAlineacionRival(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Equipo Alpha", response.getBody().getNombreEquipoRival());
    }


    @Test
    void consultarAlineacionRival_Error_PartidoNoEncontrado() {
        when(alineacionService.consultarAlineacionRival(999L, 1L))
                .thenThrow(new ResourceNotFoundException("Partido no encontrado"));

        assertThrows(ResourceNotFoundException.class,
                () -> alineacionController.consultarAlineacionRival(999L, 1L));
    }

    @Test
    void consultarAlineacionRival_Error_EquipoNoParticipa() {
        when(alineacionService.consultarAlineacionRival(1L, 99L))
                .thenThrow(new BusinessRuleException("Tu equipo no participa"));

        assertThrows(BusinessRuleException.class,
                () -> alineacionController.consultarAlineacionRival(1L, 99L));
    }

    @Test
    void consultarAlineacionRival_Error_RivalSinAlineacion() {
        when(alineacionService.consultarAlineacionRival(2L, 3L))
                .thenThrow(new ResourceNotFoundException("Rival no ha registrado alineacion"));

        assertThrows(ResourceNotFoundException.class,
                () -> alineacionController.consultarAlineacionRival(2L, 3L));
    }
}