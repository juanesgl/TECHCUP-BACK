package edu.dosw.proyect.controllers;

import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Hidden
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.debug.enabled", havingValue = "true", matchIfMissing = false)
public class DebugController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{email}")
    public UserEntity findByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}