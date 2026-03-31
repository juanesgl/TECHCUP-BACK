package edu.dosw.proyect.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CapitanInvitacionesControllerTest {

    @InjectMocks
    private CapitanInvitacionesController controller;

    @Test
    void info_RetornaOk() {
        ResponseEntity<String> result = controller.info();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("invitaciones"));
    }
}