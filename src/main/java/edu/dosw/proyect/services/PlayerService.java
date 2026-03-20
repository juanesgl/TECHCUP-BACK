package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.PlayerFilterRequest;
import edu.dosw.proyect.dtos.PlayerResponse;
import edu.dosw.proyect.models.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    private List<Player> players = new ArrayList<Player>();

    public List<PlayerResponse> filterPlayers(PlayerFilterRequest request) {
        List<PlayerResponse> result = new ArrayList<>();

        for (Player player : players) {
            boolean matches = true;

            if (request.getName() != null && !player.getName().equals(request.getName().toLowerCase().contains(request.getName().toLowerCase()))) {
                matches = false;
            }

            if (request.getPosition() != null && !player.getPosition().equalsIgnoreCase(request.getPosition())) {
                matches = false;
            }

            if (request.getAge() != null && !player.getAge().equals(request.getAge())) {
                matches = false;
            }

            if (matches){
                result.add(new PlayerResponse(player.getId(),
                        player.getName(),
                        player.getPosition(),
                        player.getAge()));
            }
        }

        return result;
    }
}
