package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;
import edu.dosw.proyect.core.models.Alineacion;
import edu.dosw.proyect.core.models.AlineacionJugador;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlineacionMapperTest {

    private final AlineacionMapper mapper = Mappers.getMapper(AlineacionMapper.class);

    private Jugador buildJugador(String nombre) {
        Jugador j = new Jugador();
        j.setNombre(nombre);
        return j;
    }

    private AlineacionJugador buildAlineacionJugador(String nombre, String rol) {
        AlineacionJugador aj = new AlineacionJugador();
        aj.setJugador(buildJugador(nombre));
        aj.setRol(rol);
        return aj;
    }

    private Alineacion buildAlineacion() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Equipo Beta");

        Alineacion a = new Alineacion();
        a.setId(1L);
        a.setEquipo(equipo);
        a.setFormacion(TacticalFormation.F_1_2_3_1);
        a.setJugadores(List.of(
                buildAlineacionJugador("J1", "TITULAR"),
                buildAlineacionJugador("J2", "TITULAR"),
                buildAlineacionJugador("J3", "TITULAR"),
                buildAlineacionJugador("J4", "TITULAR"),
                buildAlineacionJugador("J5", "TITULAR"),
                buildAlineacionJugador("J6", "TITULAR"),
                buildAlineacionJugador("J7", "TITULAR"),
                buildAlineacionJugador("J8", "RESERVA"),
                buildAlineacionJugador("J9", "RESERVA")));
        return a;
    }

    @Test
    void toRivalResponseDTO_HappyPath_MapeaCorrectamente() {
        Alineacion alineacion = buildAlineacion();

        AlineacionRivalResponseDTO dto = mapper.toRivalResponseDTO(alineacion, 1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getPartidoId());
        assertEquals("Equipo Beta", dto.getNombreEquipoRival());
        assertEquals(TacticalFormation.F_1_2_3_1, dto.getFormacion());
        assertEquals(7, dto.getTitulares().size());
        assertEquals(2, dto.getReservas().size());
        assertEquals("Alineación del equipo rival disponible", dto.getMensaje());
    }

    @Test
    void toRivalResponseDTO_SinJugadores_RetornaListasVacias() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Equipo Vacio");

        Alineacion alineacion = new Alineacion();
        alineacion.setId(2L);
        alineacion.setEquipo(equipo);
        alineacion.setFormacion(TacticalFormation.F_1_2_3_1);
        alineacion.setJugadores(List.of());

        AlineacionRivalResponseDTO dto = mapper.toRivalResponseDTO(alineacion, 2L);

        assertNotNull(dto);
        assertTrue(dto.getTitulares().isEmpty());
        assertTrue(dto.getReservas().isEmpty());
    }

    @Test
    void toRivalResponseDTO_SoloTitulares_ReservasVacias() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Equipo Solo Titulares");

        Alineacion alineacion = new Alineacion();
        alineacion.setId(3L);
        alineacion.setEquipo(equipo);
        alineacion.setFormacion(TacticalFormation.F_1_2_3_1);
        alineacion.setJugadores(List.of(
                buildAlineacionJugador("J1", "TITULAR"),
                buildAlineacionJugador("J2", "TITULAR"),
                buildAlineacionJugador("J3", "TITULAR"),
                buildAlineacionJugador("J4", "TITULAR"),
                buildAlineacionJugador("J5", "TITULAR"),
                buildAlineacionJugador("J6", "TITULAR"),
                buildAlineacionJugador("J7", "TITULAR")));

        AlineacionRivalResponseDTO dto = mapper.toRivalResponseDTO(alineacion, 3L);

        assertEquals(7, dto.getTitulares().size());
        assertTrue(dto.getReservas().isEmpty());
    }
}