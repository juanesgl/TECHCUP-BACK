package edu.dosw.proyect.controllers;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{email}")
    public User findByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}

