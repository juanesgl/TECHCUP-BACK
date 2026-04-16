package edu.dosw.proyect.controllers;

import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebugControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DebugController debugController;

    @Test
    void getAllUsers_RetornaLista() {
        UserEntity u1 = new UserEntity();
        u1.setId(1L);
        UserEntity u2 = new UserEntity();
        u2.setId(2L);
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserEntity> result = debugController.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void findByEmail_ExisteUsuario_RetornaUsuario() {
        UserEntity user = new UserEntity();
        user.setId(9L);
        when(userRepository.findByEmail("mail@escuelaing.edu.co")).thenReturn(Optional.of(user));

        UserEntity result = debugController.findByEmail("mail@escuelaing.edu.co");

        assertSame(user, result);
        verify(userRepository).findByEmail("mail@escuelaing.edu.co");
    }

    @Test
    void findByEmail_NoExiste_RetornaNull() {
        when(userRepository.findByEmail("none@escuelaing.edu.co")).thenReturn(Optional.empty());

        UserEntity result = debugController.findByEmail("none@escuelaing.edu.co");

        assertNull(result);
        verify(userRepository).findByEmail("none@escuelaing.edu.co");
    }
}
