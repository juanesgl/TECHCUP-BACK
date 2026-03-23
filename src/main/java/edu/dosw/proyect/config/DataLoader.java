package edu.dosw.proyect.config;

import edu.dosw.proyect.core.models.Student;
import edu.dosw.proyect.core.models.SportProfile;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.repositories.UserRepository;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final JugadorRepository jugadorRepository;

    @Override
    public void run(String... args) {
        log.info("Inyectando usuarios simulados para Postman...");
        
        List<String> programasValidos = Arrays.asList("sistemas", "ia", "ciberseguridad", "estadistica");

        for (long i = 1; i <= 7; i++) {
            Student student = new Student("Jugador " + i, "player"+i+"@test.com", "pass123", new SportProfile("Delantero", 90));
            student.setId(i);
            student.setProgramaAcademico(programasValidos.get((int) (i % programasValidos.size())));
            userRepository.save(student);

            Jugador j = new Jugador(i, "Jugador " + i, true, false, false);
            jugadorRepository.save(j);
        }
        
        log.info("¡7 jugadores creados con IDs 1 al 7! Usa Capitan-ID: 1 para las pruebas de Equipo.");
    }
}
