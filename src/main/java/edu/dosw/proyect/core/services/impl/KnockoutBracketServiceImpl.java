package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.response.BracketMatchDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentBracketResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.services.KnockoutBracketService;
import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnockoutBracketServiceImpl implements KnockoutBracketService {

        private static final String QUARTERFINALS = "CUARTOS";
        private static final String SEMIFINALS = "SEMIFINAL";
        private static final String FINAL = "FINAL";
        private static final String TOURNAMENT_NOT_FOUND = "Torneo no encontrado: ";

        private final TournamentRepository tournamentRepository;
        private final EquipoRepository teamRepository;
        private final LlaveEliminatoriaRepository bracketRepository;
        private final PartidoRepository matchRepository;

        @Override
        @Transactional
        public TournamentBracketResponseDTO generateBracket(String tournamentId) {
                log.info("Generando bracket para torneo: {}", tournamentId);

                TournamentEntity tournament = tournamentRepository.findByTournId(tournamentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                TOURNAMENT_NOT_FOUND + tournamentId));

                List<LlaveEliminatoriaEntity> existing = bracketRepository.findByTorneoId(tournament.getId());
                if (!existing.isEmpty()) {
                        throw new BusinessRuleException(
                                        "El bracket ya fue generado para este torneo.");
                }

                List<EquipoEntity> teams = teamRepository.findByTorneoId(tournament.getId())
                                .stream()
                                .filter(t -> "APROBADO".equalsIgnoreCase(t.getEstadoInscripcion()))
                                .toList();

                if (teams.size() < 4) {
                        throw new BusinessRuleException(
                                        "Se necesitan al menos 4 equipos inscritos para generar el bracket. "
                                                        + "Actualmente hay: " + teams.size());
                }

                Collections.shuffle(teams);

                List<LlaveEliminatoriaEntity> matchups = new ArrayList<>();

                if (teams.size() >= 8) {

                        List<EquipoEntity> top8 = teams.subList(0, 8);
                        for (int i = 0; i < 4; i++) {
                                matchups.add(buildMatchup(tournament, QUARTERFINALS, i + 1,
                                                top8.get(i * 2), top8.get(i * 2 + 1)));
                        }

                        for (int i = 0; i < 2; i++) {
                                matchups.add(buildMatchup(tournament, SEMIFINALS, i + 1, null, null));
                        }
                } else {

                        List<EquipoEntity> top4 = teams.subList(0, 4);
                        for (int i = 0; i < 2; i++) {
                                matchups.add(buildMatchup(tournament, SEMIFINALS, i + 1,
                                                top4.get(i * 2), top4.get(i * 2 + 1)));
                        }
                }

                matchups.add(buildMatchup(tournament, FINAL, 1, null, null));

                bracketRepository.saveAll(matchups);
                log.info("Bracket generado con {} llaves para torneo {} ",
                                matchups.size(), tournamentId);

                return buildBracketResponse(tournament,
                                bracketRepository.findByTorneoId(tournament.getId()));
        }

        @Override
        public TournamentBracketResponseDTO getBracket(String tournamentId) {
                TournamentEntity tournament = tournamentRepository.findByTournId(tournamentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                TOURNAMENT_NOT_FOUND + tournamentId));

                List<LlaveEliminatoriaEntity> matchups = bracketRepository.findByTorneoId(tournament.getId());
                if (matchups.isEmpty()) {
                        throw new ResourceNotFoundException(
                                        "El bracket aún no ha sido generado para el torneo: " + tournamentId);
                }

                return buildBracketResponse(tournament, matchups);
        }

        @Override
        @Transactional
        public void advanceBracket(Long matchId) {
                log.info("Avanzando bracket tras partido ID: {}", matchId);

                PartidoEntity match = matchRepository.findById(matchId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Partido no encontrado: " + matchId));

                if (match.getEstado() != MatchStatus.FINALIZADO) {
                        log.warn("El partido {} aún no está FINALIZADO, no se avanza el bracket", matchId);
                        return;
                }

                if (match.getTorneo() == null)
                        return;

                List<LlaveEliminatoriaEntity> allMatchups = bracketRepository.findByTorneoId(match.getTorneo().getId());

                LlaveEliminatoriaEntity currentMatchup = allMatchups.stream()
                                .filter(m -> m.getPartido() != null
                                                && m.getPartido().getId().equals(matchId))
                                .findFirst()
                                .orElse(null);

                if (currentMatchup == null) {
                        log.debug("El partido {} no pertenece a ninguna llave eliminatoria", matchId);
                        return;
                }

                EquipoEntity winner = match.getGolesLocal() >= match.getGolesVisitante()
                                ? match.getEquipoLocal()
                                : match.getEquipoVisitante();

                currentMatchup.setGanador(winner);
                bracketRepository.save(currentMatchup);
                log.info("Ganador de llave {} (fase {}): {}",
                                currentMatchup.getId(), currentMatchup.getFase(), winner.getNombre());

                promoteWinner(currentMatchup, winner, allMatchups);
        }

        @Override
        public List<BracketMatchDTO> getPhase(String tournamentId, String phase) {
                TournamentEntity tournament = tournamentRepository.findByTournId(tournamentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                TOURNAMENT_NOT_FOUND + tournamentId));

                return bracketRepository.findByTorneoId(tournament.getId()).stream()
                                .filter(m -> phase.equalsIgnoreCase(m.getFase()))
                                .map(this::toDTO)
                                .toList();
        }

        private LlaveEliminatoriaEntity buildMatchup(TournamentEntity tournament,
                        String phase, int number,
                        EquipoEntity team1, EquipoEntity team2) {
                LlaveEliminatoriaEntity matchup = new LlaveEliminatoriaEntity();
                matchup.setTorneo(tournament);
                matchup.setFase(phase);
                matchup.setNumeroLlave(number);
                matchup.setEquipo1(team1);
                matchup.setEquipo2(team2);
                return matchup;
        }

        private void promoteWinner(LlaveEliminatoriaEntity current,
                        EquipoEntity winner,
                        List<LlaveEliminatoriaEntity> allMatchups) {

                if (FINAL.equals(current.getFase())) {
                        log.info("El ganador del torneo es : {}\"", winner.getNombre());
                        return;
                }

                String nextPhase = QUARTERFINALS.equals(current.getFase()) ? SEMIFINALS : FINAL;
                int targetSlot = (int) Math.ceil(current.getNumeroLlave() / 2.0);

                LlaveEliminatoriaEntity target = allMatchups.stream()
                                .filter(m -> nextPhase.equals(m.getFase())
                                                && m.getNumeroLlave() == targetSlot)
                                .findFirst()
                                .orElse(null);

                if (target == null) {
                        log.warn("No se encontró llave destino en fase {} slot {}", nextPhase, targetSlot);
                        return;
                }

                if (target.getEquipo1() == null) {
                        target.setEquipo1(winner);
                } else {
                        target.setEquipo2(winner);
                }
                bracketRepository.save(target);
                log.info("{} promovido a {} llave {}", winner.getNombre(), nextPhase, targetSlot);
        }

        private TournamentBracketResponseDTO buildBracketResponse(
                        TournamentEntity tournament,
                        List<LlaveEliminatoriaEntity> matchups) {

                List<BracketMatchDTO> quarters = matchups.stream()
                                .filter(m -> QUARTERFINALS.equals(m.getFase()))
                                .sorted(Comparator.comparingInt(LlaveEliminatoriaEntity::getNumeroLlave))
                                .map(this::toDTO)
                                .toList();

                List<BracketMatchDTO> semis = matchups.stream()
                                .filter(m -> SEMIFINALS.equals(m.getFase()))
                                .sorted(Comparator.comparingInt(LlaveEliminatoriaEntity::getNumeroLlave))
                                .map(this::toDTO)
                                .toList();

                BracketMatchDTO finalMatch = matchups.stream()
                                .filter(m -> FINAL.equals(m.getFase()))
                                .map(this::toDTO)
                                .findFirst()
                                .orElse(null);

                String champion = (finalMatch != null && finalMatch.getWinnerName() != null)
                                ? finalMatch.getWinnerName()
                                : null;

                return TournamentBracketResponseDTO.builder()
                                .tournamentId(tournament.getTournId())
                                .tournamentName(tournament.getName())
                                .quarterFinals(quarters.isEmpty() ? null : quarters)
                                .semiFinals(semis.isEmpty() ? null : semis)
                                .finalMatch(finalMatch)
                                .tournamentWinner(champion)
                                .message(champion != null
                                                ? "Torneo finalizado. Campeón : " + champion
                                                : "Bracket en curso")
                                .build();
        }

        private BracketMatchDTO toDTO(LlaveEliminatoriaEntity entity) {
                return BracketMatchDTO.builder()
                                .bracketMatchId(entity.getId())
                                .phase(entity.getFase())
                                .matchNumber(entity.getNumeroLlave())
                                .team1Id(entity.getEquipo1() != null ? entity.getEquipo1().getId() : null)
                                .team1Name(entity.getEquipo1() != null
                                                ? entity.getEquipo1().getNombre()
                                                : "Por definir")
                                .team2Id(entity.getEquipo2() != null ? entity.getEquipo2().getId() : null)
                                .team2Name(entity.getEquipo2() != null
                                                ? entity.getEquipo2().getNombre()
                                                : "Por definir")
                                .matchId(entity.getPartido() != null ? entity.getPartido().getId() : null)
                                .winnerId(entity.getGanador() != null ? entity.getGanador().getId() : null)
                                .winnerName(entity.getGanador() != null
                                                ? entity.getGanador().getNombre()
                                                : null)
                                .matchStatus(entity.getPartido() != null
                                                ? entity.getPartido().getEstado().name()
                                                : "PENDIENTE")
                                .build();
        }
}