package edu.dosw.proyect.utils;

import edu.dosw.proyect.core.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @Test
    void shouldGenerateUniqueIds() {
        String id1 = IdGenerator.generateTournamentId();
        String id2 = IdGenerator.generateTournamentId();

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
        assertTrue(id1.startsWith("TOURN-"));
        assertTrue(id2.startsWith("TOURN-"));
    }

    @Test
    void shouldGenerateSequentialIds() {

        String idStr1 = IdGenerator.generateTournamentId();
        int idVal1 = Integer.parseInt(idStr1.replace("TOURN-", ""));
        
        String idStr2 = IdGenerator.generateTournamentId();
        int idVal2 = Integer.parseInt(idStr2.replace("TOURN-", ""));
        
        assertEquals(idVal1 + 1, idVal2);
    }
}
