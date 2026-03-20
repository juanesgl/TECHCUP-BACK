package edu.dosw.proyect.controllers;


import edu.dosw.proyect.dtos.PlayerFilterRequest;
import edu.dosw.proyect.dtos.PlayerResponse;
import edu.dosw.proyect.services.PlayerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private PlayerService playerService = new PlayerService();

    @PostMapping("/filter")
    public List<PlayerResponse> filterPlayers(@RequestBody PlayerFilterRequest request){
        return playerService.filterPlayers(request);
    }
}
