package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CrearEquipoResponseTest {
    @Test
    void crearEquipoResponseDTO_Builder_ConstruyeCorrectamente() {
        CrearEquipoResponseDTO dto = CrearEquipoResponseDTO.builder()
                .mensajeConfirmacion("Equipo creado exitosamente")
                .notificacionesEnviadas(List.of("Invitacion a Juan", "Invitacion a Maria"))
                .build();

        assertEquals("Equipo creado exitosamente", dto.getMensajeConfirmacion());
        assertEquals(2, dto.getNotificacionesEnviadas().size());
    }

    @Test
    void crearEquipoResponseDTO_ListaVacia_FuncionaCorrectamente() {
        CrearEquipoResponseDTO dto = CrearEquipoResponseDTO.builder()
                .mensajeConfirmacion("Equipo creado")
                .notificacionesEnviadas(List.of())
                .build();

        assertTrue(dto.getNotificacionesEnviadas().isEmpty());
    }

    @Test
    void crearEquipoResponseDTO_Setters_FuncionanCorrectamente() {
        CrearEquipoResponseDTO dto = CrearEquipoResponseDTO.builder()
                .mensajeConfirmacion("Test")
                .notificacionesEnviadas(List.of())
                .build();

        dto.setMensajeConfirmacion("Actualizado");
        assertEquals("Actualizado", dto.getMensajeConfirmacion());
    }

}
