package edu.dosw.proyect.services;

import edu.dosw.proyect.exceptions.TournamentException;
import edu.dosw.proyect.models.Tournament;
import edu.dosw.proyect.models.TournamentRequest;
import edu.dosw.proyect.models.TournamentResponse;
import edu.dosw.proyect.models.enums.TournamentsStatus;
import edu.dosw.proyect.utils.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TournamentService {

    private final Map<String, Tournament> database = Collections.synchronizedMap(new LinkedHashMap<>());

    public TournamentResponse createTournament(TournamentRequest request) {
        Tournament newTournament = new Tournament(
                null, request.name(), request.startDate(), request.endDate(),
                request.maxTeams(), request.costPerTeam(), TournamentsStatus.DRAFT, request.regulation()
        );

        newTournament.setTournId(IdGenerator.generateTournamentId());
        database.put(newTournament.getTournId(), newTournament);

        return new TournamentResponse(
                newTournament.getTournId(), newTournament.getName(),
                newTournament.getStatus(), "Tournament successfully created"
        );
    }

    public List<Tournament> getAllTournaments() {
        return new ArrayList<>(database.values());
    }

    public TournamentResponse startTournament(String turnId) {
        Tournament tournament = database.get(turnId);

        if (tournament == null) {
            throw new TournamentException("Tournament not found");
        }

        if (tournament.getStatus() == TournamentsStatus.IN_PROGRESS || tournament.getStatus() == TournamentsStatus.FINISHED) {
            throw new TournamentException("The tournament is already initialized or finished.");
        }

        tournament.setStatus(TournamentsStatus.IN_PROGRESS);
        database.put(turnId, tournament);

        return new TournamentResponse(
                tournament.getTournId(), tournament.getName(),
                tournament.getStatus(), "The tournament is now IN PROGRESS"
        );
    }

    public TournamentResponse finishTournament(String turnId) {
        Tournament tournament = database.get(turnId);

        if (tournament == null) {
            throw new TournamentException("Tournament not found");
        }

        if (tournament.getStatus() != TournamentsStatus.IN_PROGRESS) {
            throw new TournamentException("Cannot finish a tournament that is not IN PROGRESS.");
        }

        tournament.setStatus(TournamentsStatus.FINISHED);
        database.put(turnId, tournament);

        return new TournamentResponse(
                tournament.getTournId(), tournament.getName(),
                tournament.getStatus(), "The tournament has been successfully FINISHED"
        );
    }
}
