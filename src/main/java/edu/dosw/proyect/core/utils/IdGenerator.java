package edu.dosw.proyect.core.utils;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    private static final AtomicInteger counter = new AtomicInteger(1);

    public static String generateTournamentId() {
        return "TOURN-" + counter.getAndIncrement();
    }
}

