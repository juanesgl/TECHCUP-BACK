package edu.dosw.proyect.core.services;

import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.controllers.dtos.request.TournamentRequest;
import edu.dosw.proyect.controllers.dtos.response.TournamentResponse;

import java.util.List;

public interface TournamentService {

    TournamentResponse createTournament(TournamentRequest request);

    List<Tournament> getAllTournaments();

    Tournament getTournamentById(String turnId);

    TournamentResponse startTournament(String turnId) throws TournamentException;

    TournamentResponse finishTournament(String turnId) throws TournamentException;
}
