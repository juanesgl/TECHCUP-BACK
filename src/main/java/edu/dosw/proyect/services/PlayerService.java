package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.PlayerFilterRequest;
import edu.dosw.proyect.dtos.PlayerResponse;
import edu.dosw.proyect.exceptions.BusinessException;
import edu.dosw.proyect.models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private List<Player> players = new ArrayList<>();

    public PlayerService() {
        Player p1 = new Player();
        p1.setId(1L); p1.setName("Carlos Perez");
        p1.setPosition("Delantero"); p1.setAge(22);

        Player p2 = new Player();
        p2.setId(2L); p2.setName("Juan Garcia");
        p2.setPosition("Portero"); p2.setAge(25);

        Player p3 = new Player();
        p3.setId(3L); p3.setName("Luis Martinez");
        p3.setPosition("Delantero"); p3.setAge(19);

        Player p4 = new Player();
        p4.setId(4L); p4.setName("Pedro Lopez");
        p4.setPosition("Defensa"); p4.setAge(22);

        Player p5 = new Player();
        p5.setId(5L); p5.setName("Andres Torres");
        p5.setPosition("Mediocampista"); p5.setAge(30);

        players.add(p1); players.add(p2); players.add(p3);
        players.add(p4); players.add(p5);
    }

    public List<PlayerResponse> filterPlayers(PlayerFilterRequest request) {

        logger.info("Iniciando busqueda de jugadores con filtros: nombre={}, posicion={}, edad={}",
                request.getName(), request.getPosition(), request.getAge());

        if (request.getName() == null && request.getPosition() == null && request.getAge() == null) {
            logger.warn("Busqueda sin filtros - retornando todos los jugadores");
            throw new BusinessException("Debe proporcionar al menos un filtro de busqueda");
        }

        List<PlayerResponse> result = new ArrayList<>();

        for (Player player : players) {
            boolean matches = true;

            if (request.getName() != null &&
                    !player.getName().toLowerCase().contains(request.getName().toLowerCase())) {
                matches = false;
            }

            if (request.getPosition() != null &&
                    !player.getPosition().equalsIgnoreCase(request.getPosition())) {
                matches = false;
            }

            if (request.getAge() != null && !player.getAge().equals(request.getAge())) {
                matches = false;
            }

            if (matches) {
                result.add(new PlayerResponse(
                        player.getId(),
                        player.getName(),
                        player.getPosition(),
                        player.getAge()));
            }
        }

        if (result.isEmpty()) {
            logger.warn("No se encontraron jugadores con los filtros aplicados");
            throw new BusinessException("No se encontraron jugadores con los filtros indicados");
        }

        logger.info("Busqueda completada. Jugadores encontrados: {}", result.size());
        return result;
    }
}