package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.TournamentRequest;
import edu.dosw.proyect.core.models.TournamentResponse;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.repositories.TournamentRepository;
import edu.dosw.proyect.core.services.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;

    @Transactional
    public TournamentResponse createTournament(TournamentRequest request) {
        Tournament newTournament = new Tournament();
        newTournament.setName(request.name());
        newTournament.setStartDate(request.startDate());
        newTournament.setEndDate(request.endDate());
        newTournament.setMaxTeams(request.maxTeams());
        newTournament.setCostPerTeam(request.costPerTeam());
        newTournament.setStatus(TournamentsStatus.DRAFT);
        newTournament.setRegulation(request.regulation());
        newTournament.setTournId(edu.dosw.proyect.core.utils.IdGenerator.generateTournamentId());

        Tournament saved = tournamentRepository.save(newTournament);

        return new TournamentResponse(
                saved.getTournId(), saved.getName(),
                saved.getStatus(), "Tournament successfully created");
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournamentById(String turnId) {
        return tournamentRepository.findByTournId(turnId)
                .orElseThrow(() -> new TournamentException("Tournament not found"));
    }

    @Transactional
    public TournamentResponse startTournament(String turnId) {
        Tournament tournament = getTournamentById(turnId);

        if (tournament.getStatus() == TournamentsStatus.IN_PROGRESS
                || tournament.getStatus() == TournamentsStatus.FINISHED) {
            throw new TournamentException("The tournament is already initialized or finished.");
        }

        tournament.setStatus(TournamentsStatus.IN_PROGRESS);
        tournamentRepository.save(tournament);

        return new TournamentResponse(
                tournament.getTournId(), tournament.getName(),
                tournament.getStatus(), "The tournament is now IN PROGRESS");
    }

    @Transactional
    public TournamentResponse finishTournament(String turnId) {
        Tournament tournament = getTournamentById(turnId);

        if (tournament.getStatus() != TournamentsStatus.IN_PROGRESS) {
            throw new TournamentException("Cannot finish a tournament that is not IN PROGRESS.");
        }

        tournament.setStatus(TournamentsStatus.FINISHED);
        tournamentRepository.save(tournament);

        return new TournamentResponse(
                tournament.getTournId(), tournament.getName(),
                tournament.getStatus(), "The tournament has been successfully FINISHED");
    }
}

