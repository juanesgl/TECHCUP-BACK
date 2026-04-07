package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.controllers.dtos.request.TournamentRequest;
import edu.dosw.proyect.controllers.dtos.response.TournamentResponse;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
import edu.dosw.proyect.core.services.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentPersistenceMapper tournamentMapper;

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

        TournamentEntity entity = tournamentMapper.toEntity(newTournament);
        TournamentEntity saved = tournamentRepository.save(entity);
        Tournament savedDomain = tournamentMapper.toDomain(saved);

        return new TournamentResponse(
                savedDomain.getTournId(), savedDomain.getName(),
                savedDomain.getStatus(), "Tournament successfully created");
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(tournamentMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Tournament getTournamentById(String turnId) {
        return tournamentRepository.findByTournId(turnId)
                .map(tournamentMapper::toDomain)
                .orElseThrow(() -> new TournamentException("Tournament not found"));
    }

    @Transactional
    public TournamentResponse startTournament(String turnId) {
        TournamentEntity entity = tournamentRepository.findByTournId(turnId)
                .orElseThrow(() -> new TournamentException("Tournament not found"));

        if (entity.getStatus() == TournamentsStatus.IN_PROGRESS
                || entity.getStatus() == TournamentsStatus.FINISHED) {
            throw new TournamentException("The tournament is already initialized or finished.");
        }

        entity.setStatus(TournamentsStatus.IN_PROGRESS);
        tournamentRepository.save(entity);
        Tournament domain = tournamentMapper.toDomain(entity);

        return new TournamentResponse(
                domain.getTournId(), domain.getName(),
                domain.getStatus(), "The tournament is now IN PROGRESS");
    }

    @Transactional
    public TournamentResponse finishTournament(String turnId) {
        TournamentEntity entity = tournamentRepository.findByTournId(turnId)
                .orElseThrow(() -> new TournamentException("Tournament not found"));

        if (entity.getStatus() != TournamentsStatus.IN_PROGRESS) {
            throw new TournamentException("Cannot finish a tournament that is not IN PROGRESS.");
        }

        entity.setStatus(TournamentsStatus.FINISHED);
        tournamentRepository.save(entity);
        Tournament domain = tournamentMapper.toDomain(entity);

        return new TournamentResponse(
                domain.getTournId(), domain.getName(),
                domain.getStatus(), "The tournament has been successfully FINISHED");
    }
}