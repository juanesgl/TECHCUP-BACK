package edu.dosw.proyect.config;

import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final JugadorRepository jugadorRepository;
    private final PartidoRepository partidoRepository;

    @Override
    public void run(String... args) {

        log.info("Inyectando usuarios simulados para Postman...");

        List<String> programasValidos = Arrays.asList(
                "sistemas", "ia", "ciberseguridad", "estadistica"
        );

        for (int i = 1; i <= 7; i++) {
            Student student = new Student(
                    "Jugador " + i,
                    "player" + i + "@mail.escuelaing.edu.co",
                    "pass123",
                    new SportProfile("Delantero", 90)
            );
            student.setProgramaAcademico(programasValidos.get(i % programasValidos.size()));

            var saved = userRepository.save(student);

            Jugador j = new Jugador(saved.getId(), "Jugador " + i, true, false, false);
            jugadorRepository.save(j);
        }

        log.info("7 jugadores creados correctamente.");

        log.info("Creando torneo de prueba...");

        Tournament torneoBase = new Tournament(
                "TOURN-1",
                "TechCup 2026",
                LocalDate.now(),
                LocalDate.now().plusMonths(2),
                8,
                50000,
                TournamentsStatus.IN_PROGRESS,
                "Reglamento general TechCup"
        );

        log.info("Creando equipos de prueba...");

        Equipo equipoAlpha = Equipo.builder()
                .id(1L)
                .nombre("Equipo Alpha")
                .escudo("alpha.png")
                .coloresUniforme("Rojo y Blanco")
                .build();

        Equipo equipoBeta = Equipo.builder()
                .id(2L)
                .nombre("Equipo Beta")
                .escudo("beta.png")
                .coloresUniforme("Azul y Negro")
                .build();

        Equipo equipoGamma = Equipo.builder()
                .id(3L)
                .nombre("Equipo Gamma")
                .escudo("gamma.png")
                .coloresUniforme("Verde y Blanco")
                .build();

        Equipo equipoDelta = Equipo.builder()
                .id(4L)
                .nombre("Equipo Delta")
                .escudo("delta.png")
                .coloresUniforme("Amarillo y Negro")
                .build();

        log.info("Creando partidos de prueba...");

        Partido p1 = Partido.builder()
                .torneo(torneoBase)
                .equipoLocal(equipoAlpha)
                .equipoVisitante(equipoBeta)
                .nombreEquipoLocal("Equipo Alpha")
                .nombreEquipoVisitante("Equipo Beta")
                .fecha(LocalDate.now().plusDays(3))
                .hora(LocalTime.of(10, 0))
                .cancha("Cancha Principal")
                .arbitro("Carlos Medina")
                .estado(MatchStatus.PROGRAMADO)
                .golesLocal(0)
                .golesVisitante(0)
                .build();

        Partido p2 = Partido.builder()
                .torneo(torneoBase)
                .equipoLocal(equipoGamma)
                .equipoVisitante(equipoDelta)
                .nombreEquipoLocal("Equipo Gamma")
                .nombreEquipoVisitante("Equipo Delta")
                .fecha(LocalDate.now().plusDays(5))
                .hora(LocalTime.of(14, 0))
                .cancha("Cancha Norte")
                .arbitro("Luis Torres")
                .estado(MatchStatus.PROGRAMADO)
                .golesLocal(0)
                .golesVisitante(0)
                .build();

        Partido p3 = Partido.builder()
                .torneo(torneoBase)
                .equipoLocal(equipoAlpha)
                .equipoVisitante(equipoGamma)
                .nombreEquipoLocal("Equipo Alpha")
                .nombreEquipoVisitante("Equipo Gamma")
                .fecha(LocalDate.now().plusDays(7))
                .hora(LocalTime.of(16, 0))
                .cancha("Cancha Principal")
                .arbitro("Ana Rios")
                .estado(MatchStatus.PROGRAMADO)
                .golesLocal(0)
                .golesVisitante(0)
                .build();

        partidoRepository.save(p1);
        partidoRepository.save(p2);
        partidoRepository.save(p3);

        log.info("3 partidos creados: IDs 1, 2 y 3 disponibles para pruebas en Postman.");
    }
}