package edu.dosw.proyect.persistence;

import edu.dosw.proyect.persistence.repository.TeamLineupJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamLineupRepositoryTest {

    @Mock
    private TeamLineupJpaRepository repository;

    @Test
    void repositoryBean_ExisteConTipoActual() {
        assertNotNull(repository);
    }

    @Test
    void queryMethods_CompilanConRepositorioActual() {
        repository.findByTeamId(1L);
        repository.findByTeamIdAndMatchId(1L, 1L);
        repository.findByMatchId(2L);

        verify(repository).findByTeamId(1L);
        verify(repository).findByTeamIdAndMatchId(1L, 1L);
        verify(repository).findByMatchId(2L);
    }
}
