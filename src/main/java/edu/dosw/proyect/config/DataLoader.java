package edu.dosw.proyect.config;

import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        private final TournamentRepository tournamentRepository;
        private final CanchaRepository canchaRepository;
        private final EquipoRepository equipoRepository;

        @Override
        public void run(String... args) {

                log.info("Inyectando usuarios simulados para Postman...");

                List<String> programasValidos = Arrays.asList(
                                "sistemas", "ia", "ciberseguridad", "estadistica");

                for (int i = 1; i <= 7; i++) {
                        Student student = new Student(
                                        "Jugador " + i,
                                        "player" + i + "@mail.escuelaing.edu.co",
                                        "pass123",
                                        new SportProfile("Delantero", 90));
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
                                "Reglamento general TechCup");

                tournamentRepository.save(torneoBase);

                log.info("Creando equipos de prueba...");

                Equipo equipoAlpha = Equipo.builder()
                                .nombre("Equipo Alpha")
                                .escudoUrl("alpha.png")
                                .colorUniformeLocal("Rojo y Blanco")
                                .torneo(torneoBase)
                                .build();

                Equipo equipoBeta = Equipo.builder()
                                .nombre("Equipo Beta")
                                .escudoUrl("beta.png")
                                .colorUniformeLocal("Azul y Negro")
                                .torneo(torneoBase)
                                .build();

                Equipo equipoGamma = Equipo.builder()
                                .nombre("Equipo Gamma")
                                .escudoUrl("gamma.png")
                                .colorUniformeLocal("Verde y Blanco")
                                .torneo(torneoBase)
                                .build();

                Equipo equipoDelta = Equipo.builder()
                                .nombre("Equipo Delta")
                                .escudoUrl("delta.png")
                                .colorUniformeLocal("Amarillo y Negro")
                                .torneo(torneoBase)
                                .build();

                equipoRepository.saveAll(Arrays.asList(equipoAlpha, equipoBeta, equipoGamma, equipoDelta));

                log.info("Creando canchas de prueba...");
                Cancha canchaPrincipal = Cancha.builder()
                                .nombre("Cancha Principal")
                                .torneo(torneoBase)
                                .build();
                Cancha canchaNorte = Cancha.builder()
                                .nombre("Cancha Norte")
                                .torneo(torneoBase)
                                .build();
                canchaRepository.saveAll(Arrays.asList(canchaPrincipal, canchaNorte));

                log.info("Creando partidos de prueba...");

                Partido p1 = Partido.builder()
                                .torneo(torneoBase)
                                .equipoLocal(equipoAlpha)
                                .equipoVisitante(equipoBeta)
                                .fechaHora(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(10, 0)))
                                .cancha(canchaPrincipal)
                                .estado(MatchStatus.PROGRAMADO)
                                .golesLocal(0)
                                .golesVisitante(0)
                                .build();

                Partido p2 = Partido.builder()
                                .torneo(torneoBase)
                                .equipoLocal(equipoGamma)
                                .equipoVisitante(equipoDelta)
                                .fechaHora(LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.of(14, 0)))
                                .cancha(canchaNorte)
                                .estado(MatchStatus.PROGRAMADO)
                                .golesLocal(0)
                                .golesVisitante(0)
                                .build();

                Partido p3 = Partido.builder()
                                .torneo(torneoBase)
                                .equipoLocal(equipoAlpha)
                                .equipoVisitante(equipoGamma)
                                .fechaHora(LocalDateTime.of(LocalDate.now().plusDays(7), LocalTime.of(16, 0)))
                                .cancha(canchaPrincipal)
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