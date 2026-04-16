package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TournamentPersistenceMapperTest {

    private final UserPersistenceMapper userMapper = new UserPersistenceMapper();
    private final edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper mapper =
            new edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper(userMapper);

    private Tournament buildTournament() {
        Tournament t = new Tournament();
        t.setId(1L);
        t.setTournId("TOURN-1");
        t.setName("TechCup");
        t.setStatus(TournamentsStatus.DRAFT);
        t.setStartDate(LocalDate.now());
        t.setEndDate(LocalDate.now().plusMonths(3));
        t.setMaxTeams(8);
        t.setCostPerTeam(150000);
        return t;
    }

    @Test
    void toEntity_MapeaCorrectamente() {
        Tournament t = buildTournament();
        TournamentEntity entity = mapper.toEntity(t);
        assertNotNull(entity);
        assertEquals("TOURN-1", entity.getTournId());
        assertEquals(TournamentsStatus.DRAFT, entity.getStatus());
        assertEquals(8, entity.getMaxTeams());
    }

    @Test
    void toDomain_MapeaCorrectamente() {
        TournamentEntity entity = new TournamentEntity();
        entity.setId(1L);
        entity.setTournId("TOURN-1");
        entity.setName("TechCup");
        entity.setStatus(TournamentsStatus.IN_PROGRESS);
        entity.setStartDate(LocalDate.now());
        entity.setEndDate(LocalDate.now().plusMonths(3));

        Tournament domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals("TOURN-1", domain.getTournId());
        assertEquals(TournamentsStatus.IN_PROGRESS, domain.getStatus());
    }

    @Test
    void toEntity_ConOrganizador_MapeaCorrectamente() {
        Tournament t = buildTournament();
        t.setOrganizador(User.builder().id(1L).name("Org")
                .email("org@mail.com").password("p").role("ORGANIZER").build());

        TournamentEntity entity = mapper.toEntity(t);

        assertNotNull(entity.getOrganizador());
        assertEquals("Org", entity.getOrganizador().getName());
    }

    @Test
    void toEntity_Null_RetornaNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDomain_Null_RetornaNull() {
        assertNull(mapper.toDomain(null));
    }
}