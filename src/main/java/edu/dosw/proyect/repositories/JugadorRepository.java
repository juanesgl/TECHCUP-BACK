package edu.dosw.proyect.repositories;

import edu.dosw.proyect.models.Jugador;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class JugadorRepository {

    private final Map<Long, Jugador> memoria = new HashMap<>();

    public JugadorRepository() {
        memoria.put(1L, new Jugador(1L, "Lionel Messi", true, false, false));   
        memoria.put(2L, new Jugador(2L, "Cristiano Ronaldo", false, false, false)); 
        memoria.put(3L, new Jugador(3L, "Neymar Jr", true, true, false));       
    }

    public Optional<Jugador> findById(Long id) {
        return Optional.ofNullable(memoria.get(id));
    }

    public Jugador save(Jugador jugador) {
        memoria.put(jugador.getId(), jugador);
        return jugador;
    }
}
