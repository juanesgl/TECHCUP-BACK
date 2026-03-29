package edu.dosw.proyect.config;

import edu.dosw.proyect.core.models.Student;
import edu.dosw.proyect.core.models.SportProfile;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.EstadoPartido;
import edu.dosw.proyect.core.repositories.UserRepository;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
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

        log.info("7 jugadores creados. Usa los IDs retornados por /api/users/register para pruebas.");

        log.info("Creando partidos de prueba...");

        Partido p1 = Partido.builder()
                .nombreEquipoLocal("Equipo Alpha")
                .nombreEquipoVisitante("Equipo Beta")
                .fecha(LocalDate.now().plusDays(3))
                .hora(LocalTime.of(10, 0))
                .cancha("Cancha Principal")
                .arbitro("Carlos Medina")
                .estado(EstadoPartido.PROGRAMADO)
                .tournamentId("TOURN-1")
                .build();

        Partido p2 = Partido.builder()
                .nombreEquipoLocal("Equipo Gamma")
                .nombreEquipoVisitante("Equipo Delta")
                .fecha(LocalDate.now().plusDays(5))
                .hora(LocalTime.of(14, 0))
                .cancha("Cancha Norte")
                .arbitro("Luis Torres")
                .estado(EstadoPartido.PROGRAMADO)
                .tournamentId("TOURN-1")
                .build();

        Partido p3 = Partido.builder()
                .nombreEquipoLocal("Equipo Alpha")
                .nombreEquipoVisitante("Equipo Gamma")
                .fecha(LocalDate.now().plusDays(7))
                .hora(LocalTime.of(16, 0))
                .cancha("Cancha Principal")
                .arbitro("Ana Rios")
                .estado(EstadoPartido.PROGRAMADO)
                .tournamentId("TOURN-1")
                .build();

        partidoRepository.save(p1);
        partidoRepository.save(p2);
        partidoRepository.save(p3);

        log.info("3 partidos creados: IDs 1, 2 y 3 disponibles para pruebas en Postman.");
    }
}